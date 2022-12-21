package com.team12.finalproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team12.finalproject.configuration.JwtTokenFilter;
import com.team12.finalproject.domain.dto.Response;
import com.team12.finalproject.domain.dto.post.PostRequest;
import com.team12.finalproject.domain.dto.post.PostResult;
import com.team12.finalproject.service.PostService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.client.HttpClientErrorException;;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration
class PostControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostService postService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("포스트 작성 성공")
    @WithMockUser
    void write_post_s() throws Exception {
        when(postService.writePost(any(),any())).thenReturn(Response.<PostResult>builder().build());

        mockMvc.perform(post("/api/v1/posts")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PostRequest("",""))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("포스트 작성 실패 - 인증 실패")
    void write_post_f1() throws Exception {
        mockMvc.perform(post("/api/v1/posts")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PostRequest("",""))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}