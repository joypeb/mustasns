package com.team12.finalproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team12.finalproject.domain.dto.userJoin.UserJoinResponse;
import com.team12.finalproject.domain.dto.userLogin.UserLoginResponse;
import com.team12.finalproject.domain.entity.User;
import com.team12.finalproject.domain.dto.Response;
import com.team12.finalproject.domain.dto.adminRoleChange.AdminRoleChangeRequest;
import com.team12.finalproject.domain.dto.adminRoleChange.AdminRoleChangeResponse;
import com.team12.finalproject.domain.dto.userJoin.UserJoinRequest;
import com.team12.finalproject.domain.dto.userLogin.UserLoginRequest;
import com.team12.finalproject.exception.AppException;
import com.team12.finalproject.exception.ErrorCode;
import com.team12.finalproject.fixture.UserFixture;
import com.team12.finalproject.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    User userFixture;
    @BeforeEach
    void before() {
        userFixture = UserFixture.get("user","password");
    }

    @Test
    @DisplayName("회원가입 성공")
    @WithMockUser
    void join_s() throws Exception {
        when(userService.join(any()))
                .thenReturn(new UserJoinResponse(
                        1,userFixture.getUserName()));

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
        when(userService.login(any())).thenReturn(new UserLoginResponse(""));

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
        when(userService.login(any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PASSWORD,"password가 틀렸습니다"));

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest("user1","1234"))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }


    @Test
    @DisplayName("admin 권한 변경 성공")
    @WithMockUser(roles = "ADMIN")
    void admin_change_s() throws Exception{
        AdminRoleChangeResponse adminRoleChangeResponse = new AdminRoleChangeResponse("",0);

        when(userService.roleChange(0, "ADMIN"))
                .thenReturn(adminRoleChangeResponse);

        mockMvc.perform(post("/api/v1/users/1/role/change")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new AdminRoleChangeRequest("ADMIN"))))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("admin 권한 변경 실패 - 권한이 user임")
    @WithMockUser(roles = "USER")
    void admin_change_f1() throws Exception{
        AdminRoleChangeResponse adminRoleChangeResponse = new AdminRoleChangeResponse("",0);

        when(userService.roleChange(any(), any()))
                .thenReturn(adminRoleChangeResponse);

        mockMvc.perform(post("/api/v1/users/1/role/change")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new AdminRoleChangeRequest("ADMIN"))))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}