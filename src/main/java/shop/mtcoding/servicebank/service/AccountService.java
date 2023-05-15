package shop.mtcoding.servicebank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;
import shop.mtcoding.servicebank.core.exception.Exception400;
import shop.mtcoding.servicebank.core.exception.Exception404;
import shop.mtcoding.servicebank.dto.ResponseDTO;
import shop.mtcoding.servicebank.dto.account.AccountRequest;
import shop.mtcoding.servicebank.dto.account.AccountResponse;
import shop.mtcoding.servicebank.model.account.Account;
import shop.mtcoding.servicebank.model.account.AccountRepository;
import shop.mtcoding.servicebank.model.user.User;
import shop.mtcoding.servicebank.model.user.UserRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

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
}
