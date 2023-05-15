package shop.mtcoding.servicebank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.servicebank.core.exception.Exception400;
import shop.mtcoding.servicebank.core.exception.Exception401;
import shop.mtcoding.servicebank.core.exception.Exception404;
import shop.mtcoding.servicebank.core.session.SessionUser;
import shop.mtcoding.servicebank.dto.user.UserRequest;
import shop.mtcoding.servicebank.dto.user.UserResponse;
import shop.mtcoding.servicebank.model.user.User;
import shop.mtcoding.servicebank.model.user.UserRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserResponse.JoinOutDTO 회원가입(UserRequest.JoinInDTO joinInDTO) {
        Optional<User> userOP = userRepository.findByUsername(joinInDTO.getUsername());
        if (userOP.isPresent()) {
            throw new Exception400("username", "동일한 유저네임이 존재합니다");
        }

        User userPS = userRepository.save(joinInDTO.toEntity());

        return new UserResponse.JoinOutDTO(userPS);
    }

    @Transactional(readOnly = true) // select는 readOnly
    public SessionUser 로그인(UserRequest.LoginInDTO loginDTO) {

        User userPS = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new Exception404("유저네임을 찾을 수 없습니다"));

        if(!userPS.getPassword().equals(loginDTO.getPassword())) {
            throw new Exception401("패스워드 검증에 실패하였습니다.");
        }

        return new SessionUser(userPS);
    }
}
