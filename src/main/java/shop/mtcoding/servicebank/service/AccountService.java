package shop.mtcoding.servicebank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.servicebank.core.exception.Exception400;
import shop.mtcoding.servicebank.core.exception.Exception401;
import shop.mtcoding.servicebank.core.exception.Exception404;
import shop.mtcoding.servicebank.dto.ResponseDTO;
import shop.mtcoding.servicebank.dto.account.AccountRequest;
import shop.mtcoding.servicebank.dto.account.AccountResponse;
import shop.mtcoding.servicebank.dto.transaction.TransactionResponse;
import shop.mtcoding.servicebank.model.account.Account;
import shop.mtcoding.servicebank.model.account.AccountRepository;
import shop.mtcoding.servicebank.model.transaction.Transaction;
import shop.mtcoding.servicebank.model.transaction.TransactionRepository;
import shop.mtcoding.servicebank.model.user.User;
import shop.mtcoding.servicebank.model.user.UserRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public AccountResponse.SaveOutDTO 계좌등록(AccountRequest.SaveInDTO saveInDTO, Long id) {
        User userPS = userRepository.findById(id).orElseThrow(
                () -> new Exception404("유저를 찾을 수 없습니다"));

        Optional<Account> accountOP = accountRepository.findByNumber(saveInDTO.getNumber());
        if(accountOP.isPresent()) {
            throw new Exception400("number", "해당 계좌가 이미 존재 합니다.");
        }

        Account accountPS = accountRepository.save(saveInDTO.toEntity(userPS));

        return new AccountResponse.SaveOutDTO(accountPS);
    }

    public AccountResponse.DetailOutDTO 계좌상세보기(Integer number, Long userId) {
        Account accountPS = accountRepository.findByNumber(number).orElseThrow(
                () -> new Exception404("계좌를 찾을 수 없습니다")
        );

        accountPS.checkOwner(userId);

        return new AccountResponse.DetailOutDTO(accountPS);

    }

    public AccountResponse.ListOutDTO 유저계좌목록보기(Long userId) {

        User userPS = userRepository.findById(userId).orElseThrow(
                () -> new Exception404("유저를 찾을 수 없습니다"));
        List<Account> accountListPS = accountRepository.findByUserId(userId);

        return new AccountResponse.ListOutDTO(userPS, accountListPS);
    }

    @Transactional
    public AccountResponse.TransferOutDTO 계좌이체(AccountRequest.TransferInDTO transferInDTO, Long userId) {
        if(transferInDTO.getWithdrawPassword() == transferInDTO.getDepositNumber()) {
            throw new Exception400("withdrawAccountNumber", "입출금계좌가 동일할 수 없습니다");
        }

        Account withdrawAccountPS = accountRepository.findByNumber(transferInDTO.getWithdrawNumber())
                .orElseThrow(
                        () -> new Exception404("출금계좌를 찾을 수 없습니다"));
        Account depositAccountPS = accountRepository.findByNumber(transferInDTO.getDepositNumber())
                .orElseThrow(
                        () -> new Exception404("입금계좌를 찾을 수 없습니다"));

        // 출금계좌 인증(로그인한 사람과 동일한지 체크)
        withdrawAccountPS.checkOwner(userId);

        // 출금계좌 비밀번호 확인
        withdrawAccountPS.checkSamePassword(transferInDTO.getWithdrawPassword());

        // 출금계좌 잔액 확인
        withdrawAccountPS.checkBalance(transferInDTO.getAmount());

        // 이체하기
        withdrawAccountPS.withdraw(transferInDTO.getAmount());
        withdrawAccountPS.deposit(transferInDTO.getAmount());

        // 거래 내역 남기기
        Transaction transaction = Transaction.builder()
                .withdrawAccount(withdrawAccountPS)
                .depositAccount(depositAccountPS)
                .withdrawAccountBalance(withdrawAccountPS.getBalance())
                .depositAccountBalance(depositAccountPS.getBalance())
                .amount(transferInDTO.getAmount())
                .build();

        Transaction transactionPS = transactionRepository.save(transaction);

        return new AccountResponse.TransferOutDTO(withdrawAccountPS, transactionPS);
    }
}
