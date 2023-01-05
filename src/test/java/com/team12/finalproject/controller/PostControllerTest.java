package com.team12.finalproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team12.finalproject.domain.dto.Response;
import com.team12.finalproject.domain.dto.post.PostDetailResponse;
import com.team12.finalproject.domain.dto.post.PostRequest;
import com.team12.finalproject.domain.dto.post.PostResponse;
import com.team12.finalproject.domain.role.UserRole;
import com.team12.finalproject.exception.AppException;
import com.team12.finalproject.exception.ErrorCode;
import com.team12.finalproject.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(PostController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostService postService;

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserDetailsService userDetailsService;


    @Test
    @DisplayName("포스트 작성 성공")
    @WithUserDetails(value = "user1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void write_post_s() throws Exception {
        when(postService.writePost(anyString(),anyString(),anyString()))
                .thenReturn(PostResponse.response("포스트 등록 완료", 1));

        mockMvc.perform(post("/api/v1/posts")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PostRequest("","")))
                        .content("userName"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("포스트 작성 실패 - 인증 실패")
    @WithAnonymousUser
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
    @WithMockUser
    void post_list_s() throws Exception {
        mockMvc.perform(get("/api/v1/posts")
                        .with(csrf())
                .param("page","0")
                .param("size","2")
                .param("sort","createdAt,desc"))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableArg = ArgumentCaptor.forClass(Pageable.class);

        verify(postService).postList(pageableArg.capture());
        PageRequest pageable = (PageRequest) pageableArg.getValue();

        assertEquals(0,pageable.getPageNumber());
        assertEquals(2,pageable.getPageSize());
        assertEquals(Sort.by("createdAt","desc"),pageable.withSort(Sort.by("createdAt","desc")).getSort());
    }


    @Test
    @DisplayName("포스트 상세조회")
    @WithMockUser
    void post_detail_s() throws Exception {
        when(postService.detailedPost(1)).thenReturn(PostDetailResponse.builder()
                .id(1)
                .title("")
                .body("")
                .userName("")
                .build());

        mockMvc.perform(get("/api/v1/posts/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(1)))
                .andDo(print())
                .andExpect(status().isOk());

        PostDetailResponse postDetailResult = postService.detailedPost(1);

        assertNotNull(postDetailResult.getId());
        assertNotNull(postDetailResult.getTitle());
        assertNotNull(postDetailResult.getBody());
        assertNotNull(postDetailResult.getUserName());
    }

    @Test
    @DisplayName("포스트 수정 성공")
    @WithMockUser
    void post_modified_s() throws Exception{
        PostResponse postRes = PostResponse.builder().message("").postId(1).build();

        when(postService.modifyPost(1,"","","", UserRole.USER))
                .thenReturn(postRes);

        mockMvc.perform(put("/api/v1/posts/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PostRequest("t","tb"))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("포스트 수정 실패 - 인증 실패")
    @WithAnonymousUser
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
        PostRequest postRequest = new PostRequest("","");

        when(postService.modifyPost(1,"","","", UserRole.USER))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION,""));

        mockMvc.perform(put("/api/v1/posts/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PostRequest("",""))))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getHttpStatus().value()));
    }


    @Test
    @DisplayName("포스트 수정 실패 - 데이터베이스 에러")
    @WithMockUser
    void post_modified_f3() throws Exception{
        int id = 1;
        String title = "t";
        String body = "b";
        PostRequest postRequest = new PostRequest(title,body);
        String userName = "user";
        when(postService.modifyPost(id,title,body,userName,UserRole.USER))
                .thenThrow(new AppException(ErrorCode.DATABASE_ERROR,""));

        this.mockMvc.perform(put("/api/v1/posts/{id}",id)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(postRequest)))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("포스트 삭제 성공")
    @WithMockUser
    void post_delete_s() throws Exception {
        PostResponse postRes = new PostResponse("",1);
        when(postService.deletePost(1,"",UserRole.USER))
                .thenReturn(postRes);
        mockMvc.perform(delete("/api/v1/posts/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("포스트 삭제 실패 - 인증 실패")
    @WithAnonymousUser
    void post_delete_f1() throws Exception {
        PostResponse postRes = new PostResponse("",1);
        when(postService.deletePost(1,"",UserRole.USER))
                .thenReturn(postRes);

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("포스트 삭제 실패 - 포스트 찾을 수 없음")
    @WithMockUser
    void post_delete_f2() throws Exception {
        PostResponse postRes = new PostResponse("",1);
        when(postService.deletePost(1,"",UserRole.USER))
                .thenThrow(new AppException(ErrorCode.POST_NOT_FOUND,""));

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}