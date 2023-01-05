package com.team12.finalproject.controller;

import com.team12.finalproject.domain.dto.Response;
import com.team12.finalproject.domain.dto.comment.CommentRequest;
import com.team12.finalproject.domain.entity.User;
import com.team12.finalproject.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
@Api(tags = "댓글")
public class CommentController {

    private final CommentService commentService;


    //댓글 조회
    @ApiOperation(value = "댓글 목록 출력", notes = "특정 포스트의 댓글 목록을 페이징 형식으로 출력합니다")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNumber", value = "페이지 번호", required = false, dataType = "java.lang.Integer", paramType = "path", defaultValue = "0"),
            @ApiImplicitParam(name = "postId", value = "포스트 번호", required = true, dataType = "java.lang.Integer", paramType = "path", defaultValue = "None")
    })
    @GetMapping("/{postId}/comments")
    public ResponseEntity<Response> commentList(@ApiIgnore @PageableDefault(size = 10) @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, @PathVariable int postId) {
        return ResponseEntity.ok()
                .body(Response.success(commentService.commentList(postId, pageable)));
    }

    //댓글 작성
    @ApiOperation(value = "댓글 작성", notes = "특정 포스트에 댓글을 작성합니다<br>로그인한 유저만 작성이 가능합니다")
    @ApiImplicitParam(name = "postId", value = "포스트 번호", required = true, dataType = "java.lang.Integer", paramType = "path", defaultValue = "None")
    @PostMapping("/{postId}/comments")
    public ResponseEntity<Response> writeComment(@RequestBody CommentRequest commentRequest, @PathVariable int postId, @ApiIgnore @AuthenticationPrincipal User user) {
        return ResponseEntity.ok()
                .body(Response.success(commentService.writeComment(postId, user.getUserName(), commentRequest.getComment())));
    }

    //댓글 수정
    @ApiOperation(value = "댓글 수정", notes = "특정 포스트의 댓글을 수정합니다<br>댓글의 작성자만 수정이 가능합니다")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postId", value = "포스트 번호", required = true, dataType = "java.lang.Integer", paramType = "path", defaultValue = "None"),
            @ApiImplicitParam(name = "commentId", value = "댓글 번호", required = true, dataType = "java.lang.Integer", paramType = "path", defaultValue = "None")
    })
    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Response> modifyComment(@RequestBody CommentRequest commentRequest, @PathVariable int postId, @PathVariable int commentId, @ApiIgnore @AuthenticationPrincipal User user) {
        return ResponseEntity.ok()
                .body(Response.success(commentService.modifyComment(postId,commentId,user.getUserName(),commentRequest.getComment())));
    }

    //댓글 삭제
    @ApiOperation(value = "댓글 삭제", notes = "특정 포스트의 댓글을 삭제합니다<br>댓글의 작성자만 삭제가 가능합니다<br>ADMIN일경우 자유롭게 삭제가 가능합니다")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postId", value = "포스트 번호", required = true, dataType = "java.lang.Integer", paramType = "path", defaultValue = "None"),
            @ApiImplicitParam(name = "commentId", value = "댓글 번호", required = true, dataType = "java.lang.Integer", paramType = "path", defaultValue = "None")
    })
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Response> deleteComment(@PathVariable int postId, @PathVariable int commentId, @ApiIgnore @AuthenticationPrincipal User user) {
        return ResponseEntity.ok()
                .body(Response.success(commentService.deleteComment(commentId,user.getUserName(),user.getRole())));
    }
}
