package shop.mtcoding.servicebank.dto.transaction;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.servicebank.core.util.MyDateUtils;
import shop.mtcoding.servicebank.dto.account.AccountResponse;
import shop.mtcoding.servicebank.model.account.Account;
import shop.mtcoding.servicebank.model.transaction.Transaction;

import java.util.List;
import java.util.stream.Collectors;

public class TransactionResponse {

    @Getter @Setter
    public static class WithDrawOutDTO {
        private Long id;
        private Integer number;
        private Long balance;
        private String fullName;
        private List<TransactionDTO> transactions;

        public WithDrawOutDTO(Account account, List<Transaction> transactions) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
            this.fullName = account.getUser().getFullName();
            this.transactions = transactions.stream()
                    .map((transaction -> new TransactionDTO(transaction)))
                    .collect(Collectors.toList());
        }

        @Getter
        @Setter
        public class TransactionDTO {
            private Long id;
            private String sender;
            private String receiver;
            private Long amount;
            private Long balance;
            private String createdAt;

            public TransactionDTO(Transaction transaction) {
                this.id = transaction.getId();
                this.sender = transaction.getWithdrawAccount().getNumber() + "";
                this.receiver = transaction.getDepositAccount().getNumber() + "";
                this.amount = transaction.getAmount();
                this.balance = transaction.getWithdrawAccountBalance();
                this.createdAt = MyDateUtils.toStringFormat(transaction.getCreatedAt());
            }
        }
    }

    @Getter @Setter
    public static class DepositOutDTO {
        private Long id;
        private Integer number;
        private Long balance;
        private String fullName;
        private List<TransactionDTO> transactions;

        public DepositOutDTO(Account account, List<Transaction> transactions) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
            this.fullName = account.getUser().getFullName();
            this.transactions = transactions.stream()
                    .map((transaction -> new TransactionDTO(transaction)))
                    .collect(Collectors.toList());
        }

        @Getter
        @Setter
        public class TransactionDTO {
            private Long id;
            private String sender;
            private String receiver;
            private Long amount;
            private Long balance;
            private String createdAt;

            public TransactionDTO(Transaction transaction) {
                this.id = transaction.getId();
                this.sender = transaction.getWithdrawAccount().getNumber() + "";
                this.receiver = transaction.getDepositAccount().getNumber() + "";
                this.amount = transaction.getAmount();
                this.balance = transaction.getWithdrawAccountBalance();
                this.createdAt = MyDateUtils.toStringFormat(transaction.getCreatedAt());
            }
        }
    }

    // 계좌주인은 상황에 따라 다름 (출금 or 입금)
    @Setter
    @Getter
    public static class WithDrawAndDepositOutDTO {
        private Long id;
        private Integer number; // 계좌번호
        private Long balance; // 잔액
        private String fullName;
        private List<TransactionDTO> transactions;

        public WithDrawAndDepositOutDTO(Account account, List<Transaction> transactions) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
            this.fullName = account.getUser().getFullName();
            this.transactions = transactions.stream().map((transaction -> new TransactionDTO(transaction, account))).collect(Collectors.toList());
        }

        @Setter
        @Getter
        public class TransactionDTO {
            private Long id;
            private String sender;
            private String receiver;
            private Long amount;
            private Long balance;
            private String createdAt;

            public TransactionDTO(Transaction transaction, Account account) {
                this.id = transaction.getId();
                this.sender = transaction.getWithdrawAccount().getNumber() + "";
                this.receiver = transaction.getDepositAccount().getNumber() + "";
                this.amount = transaction.getAmount();
                if (account.getNumber() == transaction.getWithdrawAccount().getNumber()) {
                    this.balance = transaction.getWithdrawAccountBalance();
                } else {
                    this.balance = transaction.getDepositAccountBalance();
                }
                this.createdAt = MyDateUtils.toStringFormat(transaction.getCreatedAt());
            }
        }
    }

}
