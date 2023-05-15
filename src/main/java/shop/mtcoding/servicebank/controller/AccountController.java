package shop.mtcoding.servicebank.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import shop.mtcoding.servicebank.service.AccountService;

@RequiredArgsConstructor
@RestController
public class AccountController {
    private final AccountService accountService;
}
