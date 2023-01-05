package com.team12.finalproject.controller;

import com.team12.finalproject.domain.dto.Response;
import com.team12.finalproject.domain.entity.User;
import com.team12.finalproject.service.LikeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
@Api(tags = "좋아요")
public class LikeController {

    private final LikeService likeService;

    //좋아요 입력
    @ApiOperation(value = "좋아요 입력 | 취소", notes = "특정 게시물에 좋아요를 누릅니다<br>한번 더 호출할 경우 좋아요가 취소됩니다")
    @ApiImplicitParam(name = "postId", value = "포스트 번호", required = true, dataType = "java.lang.Integer", paramType = "path", defaultValue = "None")
    @PostMapping("/{postId}/likes")
    public ResponseEntity<Response> like(@PathVariable("postId") int postId,@ApiIgnore @AuthenticationPrincipal User user) {
        return ResponseEntity.ok()
                .body(Response.success(likeService.like(postId,user.getUserName())));
    }

    //좋아요 개수
    @ApiOperation(value = "좋아요 개수", notes = "특정 게시물에 좋아요개수를 출력합니다")
    @ApiImplicitParam(name = "postId", value = "포스트 번호", required = true, dataType = "java.lang.Integer", paramType = "path", defaultValue = "None")
    @GetMapping("/{postId}/likes")
    public ResponseEntity<Response> likeCount(@PathVariable("postId") int postId) {
        return ResponseEntity.ok()
                .body(Response.success(likeService.likeCount(postId)));
    }
}
