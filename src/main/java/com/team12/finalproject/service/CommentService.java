package com.team12.finalproject.service;

import com.team12.finalproject.domain.dto.Response;
import com.team12.finalproject.domain.dto.comment.CommentListResponse;
import com.team12.finalproject.domain.dto.comment.CommentWriteResponse;
import com.team12.finalproject.domain.entity.Comment;
import com.team12.finalproject.domain.entity.Post;
import com.team12.finalproject.domain.entity.User;
import com.team12.finalproject.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final VerificationService verificationService;
    private final CommentRepository commentRepository;

    //댓글 목록
    @Transactional
    public Response<?> commentList(int postId, Pageable pageable) {
        return Response.success(CommentListResponse.pageList(commentRepository.findAllByPostId(postId,pageable)));
    }

    //댓글 작성
    @Transactional
    public Response<?> writeComment(int postId, String userName, String comment) {
        //post가 존재하는지 확인
        Post post = verificationService.findPostById(postId);

        //user확인
        User user = verificationService.findUserByUserName(userName);

        //comment 작성
        Comment commentDetail = commentRepository.save(Comment.save(comment,post,user));

        return Response.success(CommentWriteResponse.response(commentDetail));
    }
}
