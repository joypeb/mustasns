package com.team12.finalproject.controller;

import com.team12.finalproject.domain.dto.post.PostListResponse;
import com.team12.finalproject.domain.entity.User;
import com.team12.finalproject.domain.dto.Response;
import com.team12.finalproject.domain.dto.post.PostRequest;
import com.team12.finalproject.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
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
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
@Api(tags= "포스트")
public class PostController {

    private final PostService postService;

    //포스트 목록
    @ApiOperation(value = "포스트 목록 출력", notes = "포스트 목록을 페이징 형식으로 출력합니다")
    @GetMapping
    public ResponseEntity<Response> postList(@ApiIgnore @PageableDefault(size = 20) @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(Response.success(postService.postList(pageable)));
    }

    //포스트 작성
    @ApiOperation(value = "포스트 작성", notes = "제목과 내용의 포스트를 작성합니다<br>로그인된 유저만 작성 가능합니다")
    @PostMapping
    public ResponseEntity<Response> writePost(@RequestBody PostRequest postRequest, @ApiIgnore @AuthenticationPrincipal User user) {
        return ResponseEntity.ok()
                .body(Response.success(postService.writePost(postRequest,user)));
    }

    //포스트 상세
    @ApiOperation(value = "포스트 상세보기", notes = "특정 포스트를 상세하게 볼 수 있습니다")
    @ApiImplicitParam(name = "postId", value = "포스트 번호", required = true, dataType = "java.lang.Integer", paramType = "path", defaultValue = "None")
    @GetMapping("/{postId}")
    public ResponseEntity<Response> detailedPost(@PathVariable int postId) {
        return ResponseEntity.ok()
                .body(Response.success(postService.detailedPost(postId)));
    }

    //포스트 수정
    @ApiOperation(value = "포스트 수정", notes = "특정 포스트를 수정할 수 있습니다.<br>자신의 게시글만 가능합니다.<br>ADMIN이라면 자유롭게 수정 가능합니다")
    @ApiImplicitParam(name = "postId", value = "포스트 번호", required = true, dataType = "java.lang.Integer", paramType = "path", defaultValue = "None")
    @PutMapping("/{postId}")
    public ResponseEntity<Response> modifyPost(@PathVariable int postId, @RequestBody PostRequest postRequest,@ApiIgnore @AuthenticationPrincipal User user) {
        return ResponseEntity.ok()
                .body(Response.success(postService.modifyPost(postId,postRequest,user)));
    }

    //포스트 삭제
    @ApiOperation(value = "포스트 삭제", notes = "특정 포스트를 삭제할 수 있습니다.<br>자신의 게시글만 가능합니다.<br>ADMIN이라면 자유롭게 삭제 가능합니다")
    @ApiImplicitParam(name = "postId", value = "포스트 번호", required = true, dataType = "java.lang.Integer", paramType = "path", defaultValue = "None")
    @DeleteMapping("/{postId}")
    public ResponseEntity<Response> deletePost(@PathVariable int postId, @ApiIgnore @AuthenticationPrincipal User user) {
        return ResponseEntity.ok()
                .body(Response.success(postService.deletePost(postId, user)));
    }



}
