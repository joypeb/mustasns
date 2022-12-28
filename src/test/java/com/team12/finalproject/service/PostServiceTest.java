package com.team12.finalproject.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team12.finalproject.domain.Post;
import com.team12.finalproject.domain.User;
import com.team12.finalproject.domain.dto.post.PostRequest;
import com.team12.finalproject.exception.AppException;
import com.team12.finalproject.exception.ErrorCode;
import com.team12.finalproject.fixture.PostFixture;
import com.team12.finalproject.repository.PostRepository;
import com.team12.finalproject.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PostServiceTest {

    PostService postService;

    PostRepository postRepository = Mockito.mock(PostRepository.class);
    UserRepository userRepository = Mockito.mock(UserRepository.class);

    FindUser findUser;

    @BeforeEach
    void before() {
        findUser = new FindUser(userRepository);
        postService = new PostService(postRepository,userRepository,findUser);
    }

    @Test
    @DisplayName("포스트 등록 성공")
    void post_s() {
        Post postFixture = PostFixture.get("user","1234");
        PostRequest postRequest = new PostRequest(postFixture.getTitle(), postFixture.getBody());

        Post mockPost = mock(Post.class);
        User mockUser = mock(User.class);

        when(userRepository.findByUserName(postFixture.getUser().getUserName()))
                .thenReturn(Optional.of(mockUser));
        when(postRepository.save(any()))
                .thenReturn(mockPost);

        Assertions.assertDoesNotThrow(() -> postService.writePost(postFixture.getTitle(),postFixture.getBody(), postFixture.getUser().getUserName()));
    }

    @Test
    @DisplayName("포스트 등록 실패 - username존재하지 않음")
    void post_f1() {
        Post postFixture = PostFixture.get("user","1234");
        PostRequest postRequest = new PostRequest(postFixture.getTitle(), postFixture.getBody());

        Post mockPost = mock(Post.class);
        User mockUser = mock(User.class);

        when(userRepository.findByUserName(postFixture.getUser().getUserName()))
                .thenThrow(new AppException(ErrorCode.USERNAME_NOT_FOUND,"username이 존재하지 않습니다"));
        when(postRepository.save(any()))
                .thenReturn(mockPost);

        Assertions.assertThrows(AppException.class,() -> postService.writePost(postFixture.getTitle(),postFixture.getBody(), postFixture.getUser().getUserName()));
    }

    @Test
    @DisplayName("포스트 등록 실해 - db에러")
    void post_f2() {
        Post postFixture = PostFixture.get("user","1234");
        PostRequest postRequest = new PostRequest(postFixture.getTitle(), postFixture.getBody());

        Post mockPost = mock(Post.class);
        User mockUser = mock(User.class);

        when(userRepository.findByUserName(postFixture.getUser().getUserName()))
                .thenThrow(new AppException(ErrorCode.USERNAME_NOT_FOUND,String.format("username이 존재하지 않습니다")));
        when(postRepository.save(any()))
                .thenThrow(new AppException(ErrorCode.DATABASE_ERROR,"데이터베이스 오류입니다"));

        Assertions.assertThrows(AppException.class,() -> postService.writePost(postFixture.getTitle(),postFixture.getBody(), postFixture.getUser().getUserName()));
    }


    @Test
    @DisplayName("포스트 수정 성공")
    void post_modify_s() {
        Post postFixture = PostFixture.get("user","1234");
        PostRequest postRequest = new PostRequest(postFixture.getTitle(), postFixture.getBody());

        Post mockPost = mock(Post.class);
        User mockUser = mock(User.class);

        when(postRepository.findById(postFixture.getId()))
                .thenReturn(Optional.of(mockPost));
        when(userRepository.findByUserName(postFixture.getUser().getUserName()))
                .thenReturn(Optional.of(mockUser));
        when(postRepository.save(any()))
                .thenReturn(mockPost);

        Assertions.assertDoesNotThrow(() -> postService.modifyPost(postFixture.getId(),postFixture.getTitle(),postFixture.getBody(),postFixture.getUser().getUserName()));
    }


    @Test
    @DisplayName("포스트 수정 실패 - 포스트를 찾을 수 없음")
    void post_modify_f1() {
        Post postFixture = PostFixture.get("user","1234");
        PostRequest postRequest = new PostRequest(postFixture.getTitle(), postFixture.getBody());

        Post mockPost = mock(Post.class);
        User mockUser = mock(User.class);

        when(postRepository.findById(postFixture.getId()))
                .thenThrow(new AppException(ErrorCode.POST_NOT_FOUND,"포스트가 존재하지 않습니다"));
        when(userRepository.findByUserName(postFixture.getUser().getUserName()))
                .thenReturn(Optional.of(mockUser));
        when(postRepository.save(any()))
                .thenReturn(mockPost);

        Assertions.assertThrows(AppException.class,() -> postService.modifyPost(postFixture.getId(),postFixture.getTitle(),postFixture.getBody(),postFixture.getUser().getUserName()));
    }


    @Test
    @DisplayName("포스트 수정 실패 - 작성자와 유저가 다름")
    void post_modify_f2() {
        Post postFixture = PostFixture.get("user","1234");
        PostRequest postRequest = new PostRequest(postFixture.getTitle(), postFixture.getBody());

        Post mockPost = mock(Post.class);
        User mockUser = mock(User.class);

        when(postRepository.findById(postFixture.getId()))
                .thenThrow(new AppException(ErrorCode.POST_NOT_FOUND,"포스트가 존재하지 않습니다"));
        when(userRepository.findByUserName(postFixture.getUser().getUserName()))
                .thenReturn(Optional.of(mockUser));
        when(!postFixture.getUser().getUserName().equals(mockUser.getUserName()))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION, String.format("userName 님의 포스트가 아닙니다")));
        when(postRepository.save(any()))
                .thenReturn(mockPost);

        Assertions.assertThrows(AppException.class,() -> postService.modifyPost(postFixture.getId(),postFixture.getTitle(),postFixture.getBody(),postFixture.getUser().getUserName()));
    }

    @Test
    @DisplayName("포스트 수정 실패 - 유저가 존재하지 않음")
    void post_modify_f3() {
        Post postFixture = PostFixture.get("user","1234");
        PostRequest postRequest = new PostRequest(postFixture.getTitle(), postFixture.getBody());

        Post mockPost = mock(Post.class);
        User mockUser = mock(User.class);

        when(postRepository.findById(postFixture.getId()))
                .thenReturn(Optional.of(mockPost));
        when(userRepository.findByUserName(postFixture.getUser().getUserName()))
                .thenThrow(new AppException(ErrorCode.USERNAME_NOT_FOUND,"유저가 존재하지 않습니다"));
        when(postRepository.save(any()))
                .thenReturn(mockPost);

        Assertions.assertThrows(AppException.class,() -> postService.modifyPost(postFixture.getId(),postFixture.getTitle(),postFixture.getBody(),postFixture.getUser().getUserName()));
    }

    @Test
    @DisplayName("포스트 수정 실패 - DB에러")
    void post_modify_f4() {
        Post postFixture = PostFixture.get("user","1234");
        PostRequest postRequest = new PostRequest(postFixture.getTitle(), postFixture.getBody());

        Post mockPost = mock(Post.class);
        User mockUser = mock(User.class);

        when(postRepository.findById(postFixture.getId()))
                .thenReturn(Optional.of(mockPost));
        when(userRepository.findByUserName(postFixture.getUser().getUserName()))
                .thenReturn(Optional.of(mockUser));
        when(postRepository.save(any()))
                .thenThrow(new AppException(ErrorCode.DATABASE_ERROR,"데이터베이스 에러입니다"));

        Assertions.assertThrows(AppException.class,() -> postService.modifyPost(postFixture.getId(),postFixture.getTitle(),postFixture.getBody(),postFixture.getUser().getUserName()));
    }


    @Test
    @DisplayName("포스트 삭제 성공")
    void post_delete_s() {
        Post postFixture = PostFixture.get("user","1234");
        PostRequest postRequest = new PostRequest(postFixture.getTitle(), postFixture.getBody());

        Post mockPost = mock(Post.class);
        User mockUser = mock(User.class);

        when(postRepository.findById(postFixture.getId()))
                .thenReturn(Optional.of(mockPost));
        when(userRepository.findByUserName(postFixture.getUser().getUserName()))
                .thenReturn(Optional.of(mockUser));
        when(postRepository.save(any()))
                .thenReturn(mockPost);

        Assertions.assertDoesNotThrow(() -> postService.deletePost(postFixture.getId(),postFixture.getUser().getUserName()));
    }

    @Test
    @DisplayName("포스트 삭제 실패 - 포스트가 존재하지 않음")
    void post_delete_f1() {
        Post postFixture = PostFixture.get("user","1234");
        PostRequest postRequest = new PostRequest(postFixture.getTitle(), postFixture.getBody());

        Post mockPost = mock(Post.class);
        User mockUser = mock(User.class);

        when(postRepository.findById(postFixture.getId()))
                .thenThrow(new AppException(ErrorCode.POST_NOT_FOUND,"포스트를 찾을 수 없습니다"));
        when(userRepository.findByUserName(postFixture.getUser().getUserName()))
                .thenReturn(Optional.of(mockUser));
        when(postRepository.save(any()))
                .thenReturn(mockPost);

        Assertions.assertThrows(AppException.class,() -> postService.deletePost(postFixture.getId(),postFixture.getUser().getUserName()));
    }

    @Test
    @DisplayName("포스트 삭제 실패 - 유저가 존재하지 않음")
    void post_delete_f2() {
        Post postFixture = PostFixture.get("user","1234");
        PostRequest postRequest = new PostRequest(postFixture.getTitle(), postFixture.getBody());

        Post mockPost = mock(Post.class);
        User mockUser = mock(User.class);

        when(postRepository.findById(postFixture.getId()))
                .thenReturn(Optional.of(mockPost));
        when(userRepository.findByUserName(postFixture.getUser().getUserName()))
                .thenThrow(new AppException(ErrorCode.USERNAME_NOT_FOUND,"userName을 찾을 수 없습니다"));
        when(postRepository.save(any()))
                .thenReturn(mockPost);

        Assertions.assertThrows(AppException.class,() -> postService.deletePost(postFixture.getId(),postFixture.getUser().getUserName()));
    }

}