package shop.mtcoding.servicebank.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import shop.mtcoding.servicebank.service.TransactionService;

@RequiredArgsConstructor
@RestController
public class TransactionController {
    private final TransactionService transactionService;
}
