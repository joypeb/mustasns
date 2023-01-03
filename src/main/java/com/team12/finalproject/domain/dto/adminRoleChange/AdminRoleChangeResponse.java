package com.team12.finalproject.domain.dto.adminRoleChange;

import com.team12.finalproject.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AdminRoleChangeResponse {
    private String message;
    private int userId;

    public static AdminRoleChangeResponse response(User user) {
        return AdminRoleChangeResponse.builder()
                .message(String.format("userRole이 %s로 변경되었습니다", String.valueOf(user.getRole())))
                .userId(user.getId())
                .build();
    }
}
