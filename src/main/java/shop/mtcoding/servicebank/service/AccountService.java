package shop.mtcoding.servicebank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.mtcoding.servicebank.model.account.AccountRepository;
import shop.mtcoding.servicebank.model.user.UserRepository;

@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepository accountRepository;
}
