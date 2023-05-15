package shop.mtcoding.servicebank.core.session;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.servicebank.core.util.MyDateUtils;
import shop.mtcoding.servicebank.model.user.User;

import java.time.LocalDateTime;

@Getter @Setter
public class SessionUser {
    private Long id;
    private String username;
    private String createdAt;

    public SessionUser(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.createdAt = MyDateUtils.toStringFormat(user.getCreatedAt());
    }
}
