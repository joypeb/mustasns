package com.team12.finalproject.domain.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class PostDetailResponse {
    private int id;
    private String title;
    private String body;
    private String userName;
    private String createdAt;
    private String lastModifiedAt;
}
