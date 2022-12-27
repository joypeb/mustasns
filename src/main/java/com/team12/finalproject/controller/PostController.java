package com.team12.finalproject.controller;

import com.team12.finalproject.domain.User;
import com.team12.finalproject.domain.dto.Response;
import com.team12.finalproject.domain.dto.post.PostRequest;
import com.team12.finalproject.domain.dto.post.PostResult;
import com.team12.finalproject.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<Page> postList(@PageableDefault(size = 20) @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("통과");
        return ResponseEntity.ok().body(postService.postList(pageable));
    }

    //포스트 작성
    @PostMapping
    public ResponseEntity<Response> writePost(@RequestBody PostRequest postRequest, Authentication authentication) {
        return ResponseEntity.ok().body(postService.writePost(postRequest,authentication.getName()));
    }

    //포스트 상세
    @GetMapping("/{id}")
    public ResponseEntity<Response> detailedPost(@PathVariable int id) {
        return ResponseEntity.ok().body(postService.detailedPost(id));
    }

    //포스트 수정
    @PutMapping("/{id}")
    public ResponseEntity<Response> modifyPost(@PathVariable int id, @RequestBody PostRequest postRequest, Authentication authentication) {
        Response<PostResult> response = Response.success(postService.modifyPost(id,postRequest.getTitle(),postRequest.getBody(), authentication.getName()));
        return ResponseEntity.ok().body(response);
    }

    //포스트 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deletePost(@PathVariable int id, Authentication authentication) {
        Response<PostResult> response = Response.success(postService.deletePost(id,authentication.getName()));
        return ResponseEntity.ok().body(response);
    }
}
