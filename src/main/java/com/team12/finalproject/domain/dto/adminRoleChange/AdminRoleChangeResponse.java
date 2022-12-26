package com.team12.finalproject.domain.dto.adminRoleChange;

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
}
