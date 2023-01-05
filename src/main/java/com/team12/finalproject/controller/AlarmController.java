package com.team12.finalproject.controller;

import com.team12.finalproject.domain.dto.Response;
import com.team12.finalproject.domain.entity.User;
import com.team12.finalproject.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    //로그인된 사용자의 알림 받아오기
    @GetMapping("/alarms")
    public ResponseEntity<Response> getAlarms(@PageableDefault(size = 20) @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok()
                .body(Response.success(alarmService.getAlarms(user.getUserName(), pageable)));
    }

    //특정 포스트의 알림 받아오기
    @GetMapping("{postId}/alarms")
    public ResponseEntity<Response> getPostAlarms(@PageableDefault(size = 20) @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, @PathVariable int postId) {
        return ResponseEntity.ok()
                .body(Response.success(alarmService.getPostAlarms(postId, pageable)));
    }
}
