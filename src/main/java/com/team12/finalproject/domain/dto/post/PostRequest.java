package com.team12.finalproject.domain.dto.post;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PostRequest {
    private String title;
    private String body;
}
