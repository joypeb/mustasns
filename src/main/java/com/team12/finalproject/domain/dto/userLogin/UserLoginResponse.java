package com.team12.finalproject.domain.dto.userLogin;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserLoginResponse {
    private String jwt;
}
