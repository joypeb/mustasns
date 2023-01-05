package com.team12.finalproject.domain.dto.userLogin;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequest {
    @ApiModelProperty(example = "사용자 이름")
    private String userName;
    @ApiModelProperty(example = "사용자 비밀번호")
    private String password;
}
