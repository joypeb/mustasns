package com.team12.finalproject.domain.dto.comment;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {
    @ApiModelProperty(example = "댓글내용")
    private String comment;
}
