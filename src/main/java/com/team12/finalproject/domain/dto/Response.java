package com.team12.finalproject.domain.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {
    private String resultCode;
    private T result;


    public static <T>Response error(T result) {
        return Response.builder()
                .resultCode("ERROR")
                .result(result)
                .build();
    }

    public static <T>Response success(T result) {
        return Response.builder()
                .resultCode("SUCCESS")
                .result(result)
                .build();
    }
}
