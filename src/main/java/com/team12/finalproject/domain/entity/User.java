package com.team12.finalproject.domain.entity;

import com.team12.finalproject.domain.role.UserRole;
import io.jsonwebtoken.Claims;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String userName;
    private String password;
    private String refreshToken;
    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public User(Claims claims) {
        this.userName = claims.get("userName").toString();
        this.role = UserRole.valueOf(claims.get("role").toString());
    }

    public static User save(String userName, String password) {
        return User.builder()
                .userName(userName)
                .password(password)
                .role(UserRole.USER)
                .build();
    }
}
