package shop.mtcoding.servicebank.dto.account;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.servicebank.core.util.MyDateUtils;
import shop.mtcoding.servicebank.model.account.Account;
import shop.mtcoding.servicebank.model.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AccountResponse {

    public static class SaveOutDTO {
        private Long id;
        private Integer number;
        private Long balance;

        public SaveOutDTO(Account account) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
        }
    }

    public static class DetailOutDTO {
        private Long id;
        private Integer number;
        private Long balance;
        private String fullName;
        private String createdAt;

        public DetailOutDTO(Account account) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
            this.fullName = account.getUser().getFullName();
            this.createdAt = MyDateUtils.toStringFormat(account.getCreatedAt());
        }
    }

    @Getter
    @Setter
    public static class ListOutDTO {
        private String fullName;
        private List<AccountDTO> accounts;

        // this.accounts = accounts라고 할 수 없다.
        // 두 리스트가 다르기 때문에 Account를 AccountDTO로 바꿔서 저장해야 한다.
        // this.accounts : AccountDTO
        // accounts : Account
        public ListOutDTO(User user, List<Account> accounts) {
            this.fullName = user.getFullName();
//            List<AccountDTO> accountDTOList = new ArrayList<>();
//            for(int i = 0;i < accounts.size();i++) {
//                accountDTOList.add(new AccountDTO(accounts.get(i)));
//            }
//            this.accounts = accountDTOList;

            this.accounts = accounts.stream()
                    .map(AccountDTO::new)
                    .collect(Collectors.toList());
        }

        @Getter @Setter
        public class AccountDTO {
            private Long id;
            private Integer number;
            private Long balance;

            public AccountDTO(Account account) {
                this.id = account.getId();
                this.number = account.getNumber();
                this.balance = account.getBalance();
            }
        }
    }
}
