package com.team12.finalproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team12.finalproject.configuration.JwtTokenFilter;
import com.team12.finalproject.domain.Post;
import com.team12.finalproject.domain.dto.Response;
import com.team12.finalproject.domain.dto.post.PostDetailResponse;
import com.team12.finalproject.domain.dto.post.PostRequest;
import com.team12.finalproject.domain.dto.post.PostResult;
import com.team12.finalproject.exception.AppException;
import com.team12.finalproject.exception.ErrorCode;
import com.team12.finalproject.repository.PostRepository;
import com.team12.finalproject.service.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.MockMvc.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@WebMvcTest(PostController.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@MockBean(JpaMetamodelMappingContext.class)
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

    @Test
    @DisplayName("포스트 리스트 성공")
    void post_list_s() {
        //
    }


    @Test
    @DisplayName("포스트 상세조회")
    @WithMockUser
    void post_detail_s() throws Exception {
        when(postService.detailedPost(1)).thenReturn(Response.success(PostDetailResponse.builder()
                .id(1)
                .title("")
                .body("")
                .userName("")
                .build()));

        mockMvc.perform(get("/api/v1/posts/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(1)))
                .andDo(print())
                .andExpect(status().isOk());

        Response<PostDetailResponse> postDetailResult = postService.detailedPost(1);

        Assertions.assertNotNull(postDetailResult.getResult().getId());
        Assertions.assertNotNull(postDetailResult.getResult().getTitle());
        Assertions.assertNotNull(postDetailResult.getResult().getBody());
        Assertions.assertNotNull(postDetailResult.getResult().getUserName());
    }

    @Test
    @DisplayName("포스트 수정 성공")
    @WithMockUser
    void post_modified_s() throws Exception{
        when(postService.modifyPost(1,new PostRequest("t","tb"),"")).thenReturn(new Response<>("SUCCESS",new PostResult("",1)));

        mockMvc.perform(put("/api/v1/posts/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PostRequest("t","tb"))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("포스트 수정 실패 - 인증 실패")
    void post_modified_f1() throws Exception{
        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostRequest("t","tb"))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("포스트 수정 실패 - 작성자 불일치")
    @WithMockUser
    void post_modified_f2() throws Exception{
        when(postService.modifyPost(1,new PostRequest("",""),"u"))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION,"userName이 일치하지 않습니다"));

        /*mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostRequest("t","tb"))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
*/
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/posts/{id}","1")
                .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new PostRequest("","")));

        mockMvc.perform(requestBuilder).andDo(print()).andExpect(status().isUnauthorized());
    }


    @Test
    @DisplayName("포스트 수정 실패 - 데이터베이스 에러")
    @WithMockUser
    void post_modified_f3() throws Exception{
        when(postService.modifyPost(1,new PostRequest("",""),""))
                .thenThrow(new AppException(ErrorCode.DATABASE_ERROR,"DB에러"));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostRequest("t","tb")))
                        .queryParam("id","1"))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }
}