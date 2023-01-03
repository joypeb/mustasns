package com.team12.finalproject.domain.dto.comment;

import com.team12.finalproject.domain.entity.Comment;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentWriteResponse {
    private int id;
    private String comment;
    private String userName;
    private int postId;
    private LocalDateTime createdAt;

    public static CommentWriteResponse response(Comment comment) {
        return CommentWriteResponse.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .userName(comment.getUser().getUserName())
                .postId(comment.getPost().getId())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
