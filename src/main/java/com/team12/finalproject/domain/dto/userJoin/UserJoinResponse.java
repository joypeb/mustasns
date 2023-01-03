package com.team12.finalproject.domain.dto.userJoin;

import com.team12.finalproject.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UserJoinResponse {
    private int userId;
    private String userName;

    public static UserJoinResponse response(User user) {
        return UserJoinResponse.builder()
                .userId(user.getId())
                .userName(user.getUserName())
                .build();
    }
}
