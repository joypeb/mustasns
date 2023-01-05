package com.team12.finalproject.controller;

import com.team12.finalproject.domain.dto.Response;
import com.team12.finalproject.domain.entity.User;
import com.team12.finalproject.service.LikeService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{postId}/likes")
    public ResponseEntity<Response> like(@PathVariable("postId") int postId, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok()
                .body(Response.success(likeService.like(postId,user.getUserName())));
    }

    @GetMapping("/{postId}/likes")
    public ResponseEntity<Response> likeCount(@PathVariable("postId") int postId) {
        return ResponseEntity.ok()
                .body(Response.success(likeService.likeCount(postId)));
    }
}
