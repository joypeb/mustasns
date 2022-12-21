package com.team12.finalproject.domain.dto.userLogin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequest {
    private String userName;
    private String password;
}
