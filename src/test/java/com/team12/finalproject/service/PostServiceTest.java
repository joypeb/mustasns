package com.team12.finalproject.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team12.finalproject.domain.dto.post.PostRequest;
import com.team12.finalproject.exception.AppException;
import com.team12.finalproject.exception.ErrorCode;
import com.team12.finalproject.repository.PostRepository;
import com.team12.finalproject.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostService.class)
class PostServiceTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostRepository postRepository;

    @MockBean
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("포스트 유저가 존재하지 않음")
    @WithMockUser
    void writePost() throws Exception {
        when(userRepository.findByUserName(any()))
                .thenThrow(new AppException(ErrorCode.USERNAME_NOT_FOUND,"username이 없습니다"));

        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostRequest("",""))))
                        .andDo(print())
                        .andExpect(status().isNotFound());
    }
}