package com.team12.finalproject.domain.dto.comment;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDeleteResponse {
    private String message;
    private int id;

    public static CommentDeleteResponse response(String message, int id) {
        return CommentDeleteResponse.builder()
                .message(message)
                .id(id)
                .build();
    }
}
