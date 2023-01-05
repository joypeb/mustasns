package com.team12.finalproject.controller;

import com.team12.finalproject.domain.dto.post.PostListResponse;
import com.team12.finalproject.domain.entity.User;
import com.team12.finalproject.domain.dto.Response;
import com.team12.finalproject.domain.dto.post.PostRequest;
import com.team12.finalproject.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    //포스트 목록
    @GetMapping
    public ResponseEntity<Response> postList(@PageableDefault(size = 20) @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(Response.success(postService.postList(pageable)));
    }

    //포스트 작성
    @PostMapping
    public ResponseEntity<Response> writePost(@RequestBody PostRequest postRequest, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok()
                .body(Response.success(postService.writePost(postRequest.getTitle(),postRequest.getBody(),user.getUserName())));
    }

    //포스트 상세
    @GetMapping("/{postId}")
    public ResponseEntity<Response> detailedPost(@PathVariable int postId) {
        return ResponseEntity.ok()
                .body(Response.success(postService.detailedPost(postId)));
    }

    //포스트 수정
    @PutMapping("/{postId}")
    public ResponseEntity<Response> modifyPost(@PathVariable int postId, @RequestBody PostRequest postRequest, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok()
                .body(Response.success(postService.modifyPost(postId,postRequest.getTitle(),postRequest.getBody(), user.getUserName(), user.getRole())));
    }

    //포스트 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Response> deletePost(@PathVariable int postId, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok()
                .body(Response.success(postService.deletePost(postId, user.getUserName(),user.getRole())));
    }



}
