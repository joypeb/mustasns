package com.team12.finalproject.domain.dto.adminRoleChange;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminRoleChangeRequest {
    @ApiModelProperty(example = "ADMIN | USER")
    private String role;
}
