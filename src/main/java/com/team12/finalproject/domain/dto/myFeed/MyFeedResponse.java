package com.team12.finalproject.domain.dto.myFeed;

import com.team12.finalproject.domain.dto.post.PostListResponse;
import com.team12.finalproject.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class MyFeedResponse {
    private int id;
    private String title;
    private String body;
    private String userName;
    private LocalDateTime createdAt;

    public static Page<MyFeedResponse> pageList(Page<Post> posts) {
        return posts.map(post -> MyFeedResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .body(post.getBody())
                .userName(post.getUser().getUserName())
                .createdAt(post.getCreatedAt())
                .build()
        );
    }
}
