package com.team12.finalproject.domain.dto.userJoin;

import com.team12.finalproject.domain.User;
import com.team12.finalproject.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinRequest {
    private String userName;
    private String password;

    public static User toEntity(String userName, String password, UserRole userRole) {
        return User.builder()
                .userName(userName)
                .password(password)
                .role(userRole)
                .build();
    }
}
