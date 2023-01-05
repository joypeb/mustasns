package com.team12.finalproject.controller;

import com.team12.finalproject.domain.dto.Response;
import com.team12.finalproject.domain.dto.adminRoleChange.AdminRoleChangeRequest;
import com.team12.finalproject.domain.dto.userJoin.UserJoinRequest;
import com.team12.finalproject.domain.dto.userLogin.UserLoginRequest;
import com.team12.finalproject.service.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@EnableSwagger2
@Slf4j
@Api(tags= "유저")
public class UserController {

    private final UserService userService;

    //User 회원가입
    @ApiOperation(value = "유저 회원가입", notes = "userName과 password로 회원가입을 합니다")
    @PostMapping("/join")
    public ResponseEntity<Response> join(@RequestBody UserJoinRequest userJoinRequest) {
        return ResponseEntity.ok()
                .body(Response.success(userService.join(userJoinRequest.getUserName(), userJoinRequest.getPassword())));
    }

    //User 로그인
    @ApiOperation(value = "유저 로그인", notes = "userName과 password로 로그인을 하고 JWT를 응답합니다")
    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody UserLoginRequest userLoginRequest) {
        return ResponseEntity.ok()
                .body(Response.success(userService.login(userLoginRequest.getUserName(), userLoginRequest.getPassword())));
    }

    @ApiOperation(value = "유저 권한 변경", notes = "유저의 권한을 ADMIN, USER중에 하나로 바꿀 수 있습니다. ADMIN만 가능합니다")
    @PostMapping("/{userId}/role/change")
    public ResponseEntity<?> roleChange(@ApiParam(value = "유저 ID") @PathVariable int userId, @RequestBody AdminRoleChangeRequest adminRoleChangeRequest) {
        return ResponseEntity.ok()
                .body(Response.success(userService.roleChange(userId, adminRoleChangeRequest.getRole())));
    }
}
