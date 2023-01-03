package com.team12.finalproject.domain.dto.comment;

import com.team12.finalproject.domain.dto.post.PostListResponse;
import com.team12.finalproject.domain.entity.Comment;
import com.team12.finalproject.domain.entity.Post;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentListResponse {
    private int id;
    private String comment;
    private String userName;
    private int postId;
    private LocalDateTime createdAt;

    public static Page<CommentListResponse> pageList(Page<Comment> comments) {
        return comments.map(comment -> CommentListResponse.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .userName(comment.getUser().getUserName())
                .postId(comment.getPost().getId())
                .createdAt(comment.getCreatedAt())
                .build()
        );
    }
}
