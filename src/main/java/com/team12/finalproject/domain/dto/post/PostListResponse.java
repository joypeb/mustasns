package com.team12.finalproject.domain.dto.post;

import com.team12.finalproject.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class PostListResponse {
    private int id;
    private String title;
    private String body;
    private String userName;
    private LocalDateTime createdAt;

    public static PostListResponse entity(Post post) {
        return PostListResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .body(post.getBody())
                .userName(post.getUser().getUserName())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
