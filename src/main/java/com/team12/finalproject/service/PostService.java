package com.team12.finalproject.service;

import com.team12.finalproject.domain.Post;
import com.team12.finalproject.domain.User;
import com.team12.finalproject.domain.dto.Response;
import com.team12.finalproject.domain.dto.post.PostDetailResponse;
import com.team12.finalproject.domain.dto.post.PostListResponse;
import com.team12.finalproject.domain.dto.post.PostRequest;
import com.team12.finalproject.domain.dto.post.PostResult;
import com.team12.finalproject.exception.AppException;
import com.team12.finalproject.exception.ErrorCode;
import com.team12.finalproject.repository.PostRepository;
import com.team12.finalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;



    //포스트 리스트
    public Page<PostListResponse> postList(Pageable pageable) {
        return new PageImpl<>(postRepository.findAll(pageable).stream().map(post -> PostListResponse.entity(post)).collect(Collectors.toList()));
    }

    //포스트 작성
    public Response<PostResult> writePost(PostRequest postRequest, String userName) {

        //userName이 존재하는지 확인
        User user = userRepository.findByUserName(userName).orElseThrow(
                () -> new AppException(ErrorCode.USERNAME_NOT_FOUND,String.format("%s이(가) 존재하지 않습니다",userName))
        );

        //post를 db에 저장
        Post post = postRepository.save(
                Post.builder()
                        .user(user)
                        .title(postRequest.getTitle())
                        .body(postRequest.getBody())
                        .build()
        );

        //db가 동작하지 않을때 예외처리
        if(post == null) throw new AppException(ErrorCode.DATABASE_ERROR,"데이터베이스 오류입니다");

        //포스트 등록 완료 response
        PostResult postResult = new PostResult("포스트 등록 완료", post.getId());
        return Response.success(postResult);
    }


    public Response<PostDetailResponse> detailedPost(int id) {

        //id에 대한 글을 꺼내옴
        Post post = postRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.POST_NOT_FOUND,"해당 포스트가 존재하지 않습니다")
        );

        //postDetailResponse에 결과들을 가공해서 리턴한다
        PostDetailResponse postDetailResponse = PostDetailResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .body(post.getBody())
                .userName(post.getUser().getUserName())
                .createdAt(post.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .lastModifiedAt(post.getLastModifiedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        return Response.success(postDetailResponse);
    }

    public Response<PostResult> modifyPost(int id, PostRequest postRequest, String userName) {
        //기존의 포스트를 가져오면서 포스트를 확인한다
        Post post = postRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.POST_NOT_FOUND,"포스트가 존재하지 않습니다")
        );

        //유저가 존재하는지 확인한다
        userRepository.findByUserName(userName).orElseThrow(
                () -> new AppException(ErrorCode.USERNAME_NOT_FOUND,"유저가 존재하지 않습니다")
        );

        //db에 있던 포스트의 유저와 해당 유저의 이름을 비교한다
        if(!userName.equals(post.getUser().getUserName())) {
            throw new AppException(ErrorCode.INVALID_PERMISSION,String.format("%s님의 포스트가 아닙니다",userName));
        }

        log.info("수정 내용 title " + postRequest.getTitle().equals(""));

        //내용이 비어있지 않을 경우에만 값을 추가시킨다
        if(!postRequest.getTitle().equals("") || !postRequest.getTitle().equals(null)) post.setTitle(postRequest.getTitle());
        if(!postRequest.getBody().equals("") || !postRequest.getBody().equals(null)) post.setBody(postRequest.getBody());

        //포스트를 수정한다
        Post postModified = postRepository.save(post);

        //만약 db로부터 아무것도 못받아오면 db에러를 발생시킨다
        if(postModified == null) {
            throw new AppException(ErrorCode.DATABASE_ERROR,"데이터베이스 에러입니다");
        }

        //postResult에 결과를 담아 리턴한다
        PostResult postResult = new PostResult("포스트 수정 완료", postModified.getId());
        return Response.success(postResult);
    }
}
