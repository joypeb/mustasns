package com.team12.finalproject.domain.dto.userJoin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UserJoinResponse {
    private String resultCode;
    private UserJoinResult result;
}
