package com.team12.finalproject.domain.dto.userJoin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UserJoinResponse<T> {
    private String resultCode;
    private T result;
}
