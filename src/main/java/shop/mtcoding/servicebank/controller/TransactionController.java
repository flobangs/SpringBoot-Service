package shop.mtcoding.servicebank.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.mtcoding.servicebank.core.exception.Exception400;
import shop.mtcoding.servicebank.core.exception.Exception401;
import shop.mtcoding.servicebank.core.session.SessionUser;
import shop.mtcoding.servicebank.dto.ResponseDTO;
import shop.mtcoding.servicebank.service.TransactionService;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@RestController
public class TransactionController {
    private final TransactionService transactionService;
    private final HttpSession session;

    // {number} : 계좌번호 (계좌번호의 transaction을 줘)
    // @ReqeustParam(defaultValue = "all") : 입출금 내역 모두 줘
    // 주소로 대화를 할 수 있도록 작성해야 한다. RestAPI
    // 계좌 7316의 트랜젝션 중에서 입금과 출금내역 모두 줘
    // 여기서는 all, deposit, withdraw를 모았지만
    // controller별로 분리할 수도 있다.
    // @GetMapping("/account/{number}/transaction/withdraw")
    @GetMapping("/account/{number}/transaction")
    public ResponseEntity<?> findTransaction(@PathVariable Integer number,
                                             @RequestParam(defaultValue = "all") String gubun) {

        SessionUser sessionUser = (SessionUser)session.getAttribute("sessionUser");
        if(sessionUser == null) {
            throw new Exception401("인증되지 않은 사용자 입니다");
        }

        if(gubun.equals("all")) {
            ResponseDTO<?> responseDTO = new ResponseDTO<>().data(
                    transactionService.입출금내역보기(number, sessionUser.getId()));
            return ResponseEntity.ok(responseDTO);
        } else if(gubun.equals("withdraw")) {
            ResponseDTO<?> responseDTO = new ResponseDTO<>().data(
                    transactionService.입금내역보기(number, sessionUser.getId()));
            return ResponseEntity.ok(responseDTO);
        } else if(gubun.equals("deposit")) {
            ResponseDTO<?> responseDTO = new ResponseDTO<>().data(
                    transactionService.출금내역보기(number, sessionUser.getId()));
            return ResponseEntity.ok(responseDTO);
        } else {
            throw new Exception400("gubun", "잘못된 요청을 하였습니다");
        }

    }
}
