package com.team12.finalproject.service;

import com.team12.finalproject.domain.dto.Response;
import com.team12.finalproject.domain.dto.comment.CommentDeleteResponse;
import com.team12.finalproject.domain.dto.comment.CommentListResponse;
import com.team12.finalproject.domain.dto.comment.CommentResponse;
import com.team12.finalproject.domain.entity.Comment;
import com.team12.finalproject.domain.entity.Post;
import com.team12.finalproject.domain.entity.User;
import com.team12.finalproject.domain.role.UserRole;
import com.team12.finalproject.exception.AppException;
import com.team12.finalproject.exception.ErrorCode;
import com.team12.finalproject.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final VerificationService verificationService;
    private final CommentRepository commentRepository;

    //댓글 목록
    @Transactional
    public Response<CommentListResponse> commentList(int postId, Pageable pageable) {
        return Response.success(CommentListResponse.pageList(commentRepository.findAllByPostIdAndDeletedAtIsNull(postId,pageable)));
    }

    //댓글 작성
    @Transactional
    public Response<CommentResponse> writeComment(int postId, String userName, String comment) {
        //post가 존재하는지 확인
        Post post = verificationService.findPostById(postId);

        //user확인
        User user = verificationService.findUserByUserName(userName);

        //comment 작성
        Comment commentDetail = commentRepository.save(Comment.save(comment,post,user));

        return Response.success(CommentResponse.response(commentDetail));
    }

    public Response<?> modifyComment(int postId, int commentId, String userName, String comment) {
        //post가 존재하는지 확인
        Post post = verificationService.findPostById(postId);

        //댓글이 존재하는지 확인
        Comment commentDetail = verificationService.findCommentById(commentId);

        //유저가 일치하는지 확인
        verificationService.checkSameUserName(userName,commentDetail.getUser().getUserName(),"댓글");

        //댓글 수정
        commentDetail.setComment(comment);
        Comment savedComment = commentRepository.save(commentDetail);

        //db에러 체크
        verificationService.checkDB(savedComment);

        return Response.success(CommentResponse.response(savedComment));
    }


    //댓글 삭제
    public Response<CommentDeleteResponse> deleteComment(int commentId, String userName, UserRole role) {
        //댓글이 존재하는지 확인
        Comment commentDetail = verificationService.findCommentById(commentId);

        //유저가 일치하는지 확인
        //ADMIN일경우 스킵
        if(UserRole.USER.equals(role))
            verificationService.checkSameUserName(userName,commentDetail.getUser().getUserName(),"댓글");

        //댓글 삭제
        commentDetail.setDeletedAt(LocalDateTime.now());
        Comment deletedComment = commentRepository.save(commentDetail);

        //db에러 체크
        if(deletedComment.getDeletedAt() == null)
            throw new AppException(ErrorCode.DATABASE_ERROR,"DB에러입니다");

        return Response.success(CommentDeleteResponse.response("댓글 삭제 완료",commentId));
    }
}
