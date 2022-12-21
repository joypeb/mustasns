package com.team12.finalproject.controller;

import com.team12.finalproject.domain.dto.Response;
import com.team12.finalproject.domain.dto.post.PostRequest;
import com.team12.finalproject.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @GetMapping
    public String postList(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable) {

        return "";
    }

    @PostMapping
    public ResponseEntity<Response> writePost(@RequestBody PostRequest postRequest, Authentication authentication) {
        return ResponseEntity.ok().body(postService.writePost(postRequest,authentication.getName()));
    }
}
