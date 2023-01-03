package com.team12.finalproject.domain.dto.comment;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {
    private String comment;
}
