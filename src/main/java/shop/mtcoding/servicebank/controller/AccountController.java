package shop.mtcoding.servicebank.controller;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.servicebank.core.exception.Exception401;
import shop.mtcoding.servicebank.core.exception.Exception403;
import shop.mtcoding.servicebank.core.session.SessionUser;
import shop.mtcoding.servicebank.dto.ResponseDTO;
import shop.mtcoding.servicebank.dto.account.AccountRequest;
import shop.mtcoding.servicebank.dto.account.AccountResponse;
import shop.mtcoding.servicebank.service.AccountService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class AccountController {
    private final AccountService accountService;
    private final HttpSession session;

    @PostMapping("/account")
    public ResponseEntity<?> saveAccount(@RequestBody @Valid AccountRequest.SaveInDTO saveInDTO, Errors errors) {
        SessionUser sessionUser = (SessionUser)session.getAttribute("sessionUser");
        if(sessionUser == null) {
            throw new Exception401("인증되지 않은 사용자 입니다");
        }

        AccountResponse.SaveOutDTO saveOutDTO = accountService.계좌등록(saveInDTO, sessionUser.getId());
        ResponseDTO<?> responseDTO = new ResponseDTO<>().data(saveOutDTO);

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/account/{number}")
    public ResponseEntity<?> findAccountDetail(@PathVariable Integer number) {
        SessionUser sessionUser = (SessionUser)session.getAttribute("sessionUser");
        if(sessionUser == null) {
            throw new Exception401("인증되지 않은 사용자 입니다");
        }

        AccountResponse.DetailOutDTO detailOutDTO = accountService.계좌상세보기(number, sessionUser.getId());
        ResponseDTO<?> responseDTO = new ResponseDTO<>().data(detailOutDTO);

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/account")
    public ResponseEntity<?> findUserAccountList(Long userId) {
        SessionUser sessionUser = (SessionUser)session.getAttribute("sessionUser");
        if(sessionUser == null) {
            throw new Exception401("인증되지 않은 사용자 입니다");
        }

        if(sessionUser.getId() != userId.longValue()) {
            throw new Exception403("권한이 없습니다");
        }

        AccountResponse.ListOutDTO listOutDTO = accountService.유저계좌목록보기(userId);
        ResponseDTO<?> responseDTO = new ResponseDTO<>().data(listOutDTO);

        return ResponseEntity.ok().body(responseDTO);
    }
}
