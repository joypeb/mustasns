package com.team12.finalproject.service;

import com.team12.finalproject.domain.dto.post.PostRequest;
import com.team12.finalproject.domain.dto.post.PostResponse;
import com.team12.finalproject.domain.entity.Post;
import com.team12.finalproject.domain.entity.User;
import com.team12.finalproject.domain.role.UserRole;
import com.team12.finalproject.domain.dto.Response;
import com.team12.finalproject.domain.dto.post.PostDetailResponse;
import com.team12.finalproject.domain.dto.post.PostListResponse;
import com.team12.finalproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
    public PostResponse writePost(PostRequest postRequest, User user) {
        //userName이 존재하는지 확인
        User findUser = verificationService.findUserByUserName(user.getUserName());

        //post를 db에 저장 후 db체크
        Post post = postRepository.save(Post.save(postRequest,findUser));
        verificationService.checkDB(post);

        return PostResponse.response("포스트 등록 완료", post.getId());
    }


    //포스트 상세
    @Transactional
    public PostDetailResponse detailedPost(int postId) {
        //id에 대한 글을 꺼내옴
        //삭제된 포스트인지 확인
        Post post = verificationService.findPostById(postId);

        return PostDetailResponse.response(post);
    }

    //포스트 수정
    @Transactional
    public PostResponse modifyPost(int postId,PostRequest postRequest, User user) {
        //기존의 포스트를 가져오면서 포스트를 확인한다
        Post post = verificationService.findPostById(postId);

        //db에 있던 포스트의 유저와 해당 유저의 이름을 비교한다
        //admin일경우 스킵한다
        if(UserRole.USER.equals(user.getRole())) {
            verificationService.checkSameUserName(post.getUser().getUserName(),user.getUserName(),"포스트");
        }

        //내용이 비어있지 않을 경우에만 값을 추가시킨다
        if(!postRequest.getTitle().equals("") && !postRequest.getTitle().equals(null)) post.setTitle(postRequest.getTitle());
        if(!postRequest.getBody().equals("") && !postRequest.getBody().equals(null)) post.setBody(postRequest.getBody());

        //포스트를 수정한다
        Post postModified = postRepository.save(post);
        verificationService.checkDB(postModified);

        //postResult에 결과를 담아 리턴한다
        return PostResponse.response("포스트 수정 완료", postModified.getId());
    }

    //포스트 삭제
    @Transactional
    public PostResponse deletePost(int postId, User user) {

        //해당 아이디의 포스트가 존재하는지 확인
        Post post = verificationService.findPostById(postId);

        //해당 포스트의 userNamer과 요청된 userName이 일치하는지 확인
        //admin일경우 스킵
        if(UserRole.USER.equals(user.getRole())) {
            verificationService.checkSameUserName(post.getUser().getUserName(), user.getUserName(), "포스트");
        }

        //delete실행
        //실제 삭제하는것이 아닌 deletedAt을 null이 아니게 만든다
        postRepository.delete(post);

        return PostResponse.response("포스트 삭제 완료",postId);
    }



}
