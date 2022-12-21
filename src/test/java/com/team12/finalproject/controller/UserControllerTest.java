package com.team12.finalproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team12.finalproject.domain.dto.Response;
import com.team12.finalproject.domain.dto.userJoin.UserJoinRequest;
import com.team12.finalproject.domain.dto.userJoin.UserJoinResponse;
import com.team12.finalproject.domain.dto.userJoin.UserJoinResult;
import com.team12.finalproject.domain.dto.userLogin.UserLoginRequest;
import com.team12.finalproject.exception.AppException;
import com.team12.finalproject.exception.ErrorCode;
import com.team12.finalproject.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 성공")
    @WithMockUser
    void join_s() throws Exception {
        UserJoinRequest userJoinRequest = new UserJoinRequest("user1","1234");
        when(userService.join(userJoinRequest))
                .thenReturn(new Response<UserJoinResult>("SUCCESS",new UserJoinResult(
                        1,userJoinRequest.getUserName())));

        mockMvc.perform(post("/api/v1/users/join")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserJoinRequest())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원가입 실패 유저 중복")
    @WithMockUser
    void join_f() throws Exception {
        when(userService.join(any()))
                .thenThrow(new AppException(ErrorCode.DUPLICATED_USER_NAME, "UserName이 중복됩니다"));

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest())))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("로그인 성공")
    @WithMockUser
    void login_s() throws Exception {
        Map<String,String> map = new HashMap<>();
        when(userService.login(new UserLoginRequest("user1","1234"))).thenReturn(map);

        mockMvc.perform(post("/api/v1/users/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserLoginRequest("user1","1234"))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 실패 - username틀림")
    @WithMockUser
    void login_f1() throws Exception{
        when(userService.login(any())).thenThrow(
                new AppException(ErrorCode.USERNAME_NOT_FOUND,"userName이 틀렸습니다")
        );

        mockMvc.perform(post("/api/v1/users/login")
                .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserLoginRequest("user1","1234"))))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("로그인 실패 - password틀림")
    @WithMockUser
    void login_f2() throws Exception{
        when(userService.login(any())).thenThrow(
                new AppException(ErrorCode.INVALID_PASSWORD,"password가 틀렸습니다")
        );

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest("user1","1234"))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}