package shop.mtcoding.servicebank.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import shop.mtcoding.servicebank.service.UserService;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
}
