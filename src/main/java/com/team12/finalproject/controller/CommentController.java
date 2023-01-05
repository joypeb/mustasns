package com.team12.finalproject.controller;

import com.team12.finalproject.domain.dto.Response;
import com.team12.finalproject.domain.dto.comment.CommentRequest;
import com.team12.finalproject.domain.entity.User;
import com.team12.finalproject.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class CommentController {

    private final CommentService commentService;


    //댓글 조회
    @GetMapping("/{postId}/comments")
    public ResponseEntity<Response> commentList(@PageableDefault(size = 10) @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, @PathVariable int postId) {
        return ResponseEntity.ok()
                .body(Response.success(commentService.commentList(postId, pageable)));
    }

    //댓글 작성
    @PostMapping("/{postId}/comments")
    public ResponseEntity<Response> writeComment(@RequestBody CommentRequest commentRequest, @PathVariable int postId, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok()
                .body(Response.success(commentService.writeComment(postId, user.getUserName(), commentRequest.getComment())));
    }

    //댓글 수정
    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Response> modifyComment(@RequestBody CommentRequest commentRequest, @PathVariable int postId, @PathVariable int commentId, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok()
                .body(Response.success(commentService.modifyComment(postId,commentId,user.getUserName(),commentRequest.getComment())));
    }

    //댓글 삭제
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Response> deleteComment(@PathVariable int postId, @PathVariable int commentId, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok()
                .body(Response.success(commentService.deleteComment(commentId,user.getUserName(),user.getRole())));
    }
}
