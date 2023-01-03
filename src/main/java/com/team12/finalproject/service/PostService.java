package com.team12.finalproject.service;

import com.team12.finalproject.domain.dto.post.PostResponse;
import com.team12.finalproject.domain.entity.Post;
import com.team12.finalproject.domain.entity.User;
import com.team12.finalproject.domain.role.UserRole;
import com.team12.finalproject.domain.dto.Response;
import com.team12.finalproject.domain.dto.post.PostDetailResponse;
import com.team12.finalproject.domain.dto.post.PostListResponse;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final VerificationService verificationService;



    //포스트 리스트
    @Transactional
    public Page<PostListResponse> postList(Pageable pageable) {
        return PostListResponse.pageList(postRepository.findAll(pageable));
    }

    //포스트 작성
    @Transactional
    public Response<PostResponse> writePost(String title, String body, String userName) {
        //userName이 존재하는지 확인
        User user = verificationService.findUserByUserName(userName);

        //post를 db에 저장 후 db체크
        Post post = postRepository.save(Post.save(user,title,body));
        verificationService.checkDB(post);

        return Response.success(PostResponse.response("포스트 등록 완료", post.getId()));
    }


    //포스트 상세
    @Transactional
    public Response<PostDetailResponse> detailedPost(int id) {
        //id에 대한 글을 꺼내옴
        Post post = verificationService.findPostById(id);

        return Response.success(PostDetailResponse.response(post));
    }

    //포스트 수정
    @Transactional
    public Response<PostResponse> modifyPost(int id, String title, String body, String userName, UserRole userRole) {
        //기존의 포스트를 가져오면서 포스트를 확인한다
        Post post = verificationService.findPostById(id);

        //db에 있던 포스트의 유저와 해당 유저의 이름을 비교한다
        //admin일경우 스킵한다
        if(UserRole.USER.equals(userRole)) {
            verificationService.checkSameUserName(post.getUser().getUserName(),userName);
        }

        //내용이 비어있지 않을 경우에만 값을 추가시킨다
        if(!title.equals("") && !title.equals(null)) post.setTitle(title);
        if(!body.equals("") && !body.equals(null)) post.setBody(body);

        //포스트를 수정한다
        Post postModified = postRepository.save(post);
        verificationService.checkDB(postModified);

        //postResult에 결과를 담아 리턴한다
        return Response.success(PostResponse.response("포스트 수정 완료", postModified.getId()));
    }

    //포스트 삭제
    @Transactional
    public Response<PostResponse> deletePost(int id, String userName, UserRole userRole) {

        //해당 아이디의 포스트가 존재하는지 확인
        Post post = verificationService.findPostById(id);

        //해당 포스트의 userNamer과 요청된 userName이 일치하는지 확인
        //admin일경우 스킵
        if(UserRole.USER.equals(userRole)) {
            verificationService.checkSameUserName(post.getUser().getUserName(),userName);
        }

        //delete실행
        postRepository.deleteById(id);

        //delete 삭제 확인
        postRepository.findById(id).ifPresent(
                (post1 -> new AppException(ErrorCode.DATABASE_ERROR,"DB에러입니다"))
        );

        return Response.success(PostResponse.response("포스트 삭제 완료",id));
    }



}
