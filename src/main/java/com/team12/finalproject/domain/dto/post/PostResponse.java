package com.team12.finalproject.domain.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PostResponse {
    private String message;
    private int postId;

    public static PostResponse response(String message, int postId) {
        return PostResponse.builder()
                .message(message)
                .postId(postId)
                .build();
    }
}
