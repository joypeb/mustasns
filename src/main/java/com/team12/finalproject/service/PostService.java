package com.team12.finalproject.service;

import com.team12.finalproject.domain.Post;
import com.team12.finalproject.domain.dto.Response;
import com.team12.finalproject.domain.dto.post.PostRequest;
import com.team12.finalproject.domain.dto.post.PostResult;
import com.team12.finalproject.exception.AppException;
import com.team12.finalproject.exception.ErrorCode;
import com.team12.finalproject.repository.PostRepository;
import com.team12.finalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;



    //포스트 리스트
    public Page<Post> postList(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    //포스트 작성
    public Response<PostResult> writePost(PostRequest postRequest, String userName) {

        //userName이 존재하는지 확인
        userRepository.findByUserName(userName).orElseThrow(
                () -> new AppException(ErrorCode.USERNAME_NOT_FOUND,String.format("%s이(가) 존재하지 않습니다",userName))
        );

        //post를 db에 저장
        Post post = postRepository.save(
                Post.builder()
                        .userName(userName)
                        .title(postRequest.getTitle())
                        .body(postRequest.getBody())
                        .registeredAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        );

        //db가 동작하지 않을때 예외처리
        if(post == null) throw new AppException(ErrorCode.DATABASE_ERROR,"데이터베이스 오류입니다");

        //포스트 등록 완료 response
        PostResult postResult = new PostResult("포스트 등록 완료", post.getId());
        return Response.success(postResult);
    }



}
