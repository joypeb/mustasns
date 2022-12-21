package com.team12.finalproject.controller;

import com.team12.finalproject.domain.dto.Response;
import com.team12.finalproject.domain.dto.userJoin.UserJoinRequest;
import com.team12.finalproject.domain.dto.userJoin.UserJoinResponse;
import com.team12.finalproject.domain.dto.userLogin.UserLoginRequest;
import com.team12.finalproject.domain.dto.userLogin.UserLoginResponse;
import com.team12.finalproject.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    //User 회원가입
    @PostMapping("/join")
    public ResponseEntity<Response> join(@RequestBody UserJoinRequest userJoinRequest) {
        return ResponseEntity.ok().body(userService.join(userJoinRequest));
    }

    //User 로그인
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest userLoginRequest) {
        Map<String,String> tokenMap = userService.login(userLoginRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("token", tokenMap.get("token"));
        headers.add("refreshToken", tokenMap.get("refreshToken"));
        return ResponseEntity.ok()
                .headers(headers)
                .body(UserLoginResponse.builder().jwt(tokenMap.get("token")).build());
    }
}
