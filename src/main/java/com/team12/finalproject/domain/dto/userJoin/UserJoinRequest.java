package com.team12.finalproject.domain.dto.userJoin;

import com.team12.finalproject.domain.entity.User;
import com.team12.finalproject.domain.role.UserRole;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinRequest {

    @ApiModelProperty(example = "사용자 이름")
    private String userName;
    @ApiModelProperty(example = "사용자 비밀번호")
    private String password;

}
