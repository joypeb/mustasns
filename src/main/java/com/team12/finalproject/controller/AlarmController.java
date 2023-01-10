package com.team12.finalproject.controller;

import com.team12.finalproject.domain.dto.Response;
import com.team12.finalproject.domain.entity.User;
import com.team12.finalproject.service.AlarmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Api(tags = "알림")
public class AlarmController {

    private final AlarmService alarmService;

    //로그인된 사용자의 알림 받아오기
    @ApiOperation(value = "유저 알림 출력", notes = "특정 유저의 알림들의 목록을 페이징 형식으로 출력합니다")
    @ApiImplicitParam(name = "pageNumber", value = "페이지 번호", required = false, dataType = "java.lang.Integer", paramType = "path", defaultValue = "0")
    @GetMapping("/alarms")
    public ResponseEntity<Response> getAlarms(@ApiIgnore @PageableDefault(size = 20) @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,@ApiIgnore @AuthenticationPrincipal User user) {
        return ResponseEntity.ok()
                .body(Response.success(alarmService.getAlarms(user.getUserName(), pageable)));
    }

    //특정 포스트의 알림 받아오기
    @ApiOperation(value = "포스트 알림 출력", notes = "특정 포스트의 알림들의 목록을 페이징 형식으로 출력합니다")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNumber", value = "페이지 번호", required = false, dataType = "java.lang.Integer", paramType = "path", defaultValue = "0"),
            @ApiImplicitParam(name = "postId", value = "포스트 번호", required = true, dataType = "java.lang.Integer", paramType = "path", defaultValue = "None")
    })
    @GetMapping("{postId}/alarms")
    public ResponseEntity<Response> getPostAlarms(@ApiIgnore @PageableDefault(size = 20) @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, @PathVariable int postId) {
        return ResponseEntity.ok()
                .body(Response.success(alarmService.getPostAlarms(postId, pageable)));
    }
}
