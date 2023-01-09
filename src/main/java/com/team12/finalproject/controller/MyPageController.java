package com.team12.finalproject.controller;

import com.team12.finalproject.domain.dto.Response;
import com.team12.finalproject.domain.entity.User;
import com.team12.finalproject.service.MyPageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
@Api(tags= "마이 피드")
public class MyPageController {

    private final MyPageService myPageService;
    @ApiOperation(value = "마이 피드 출력", notes = "자신이 작성한 글들을 페이징 형식으로 출력합니다<br>로그인한 유저만 사용 가능합니다")
    @ApiImplicitParam(name = "pageNumber", value = "페이지 번호", required = false, dataType = "java.lang.Integer", paramType = "path", defaultValue = "0")
    @GetMapping("/my")
    public ResponseEntity<Response> myFeed(@ApiIgnore @AuthenticationPrincipal User user,@ApiIgnore @PageableDefault(size = 20) @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok()
                .body(Response.success(myPageService.myFeed(user, pageable)));
    }
}
