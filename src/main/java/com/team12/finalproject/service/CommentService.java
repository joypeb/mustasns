package com.team12.finalproject.service;

import com.team12.finalproject.domain.dto.Response;
import com.team12.finalproject.domain.dto.comment.CommentDeleteResponse;
import com.team12.finalproject.domain.dto.comment.CommentListResponse;
import com.team12.finalproject.domain.dto.comment.CommentRequest;
import com.team12.finalproject.domain.dto.comment.CommentResponse;
import com.team12.finalproject.domain.entity.Alarm;
import com.team12.finalproject.domain.entity.Comment;
import com.team12.finalproject.domain.entity.Post;
import com.team12.finalproject.domain.entity.User;
import com.team12.finalproject.domain.role.AlarmType;
import com.team12.finalproject.domain.role.UserRole;
import com.team12.finalproject.exception.AppException;
import com.team12.finalproject.exception.ErrorCode;
import com.team12.finalproject.repository.AlarmRepository;
import com.team12.finalproject.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CommentService {

    private final VerificationService verificationService;
    private final CommentRepository commentRepository;

    //댓글 목록
    @Transactional
    public Page<CommentListResponse> commentList(int postId, Pageable pageable) {
        return CommentListResponse.pageList(commentRepository.findAllByPostId(postId,pageable));
    }

    //댓글 작성
    @Transactional
    public CommentResponse writeComment(int postId, CommentRequest commentRequest, User user) {
        //post가 존재하는지 확인
        Post post = verificationService.findPostById(postId);

        //user확인
        User findUser = verificationService.findUserByUserName(user.getUserName());

        //comment 작성
        Comment commentDetail = commentRepository.save(Comment.save(commentRequest.getComment(),post,findUser));
        verificationService.checkDB(commentDetail);

        //comment 작성 완료시 알림 발생
        //자신이 쓴 댓글일 경우 알림을 발생시키지 않는다
        verificationService.makeAlarm(AlarmType.NEW_COMMENT_ON_POST,post,findUser);

        return CommentResponse.response(commentDetail);
    }

    public CommentResponse modifyComment(int postId, int commentId,CommentRequest commentRequest, User user) {
        //post가 존재하는지 확인
        Post post = verificationService.findPostById(postId);

        //댓글이 존재하는지 확인
        Comment commentDetail = verificationService.findCommentById(commentId);

        //유저가 일치하는지 확인
        verificationService.checkSameUserName(user.getUserName(),commentDetail.getUser().getUserName(),"댓글");

        //댓글 수정
        commentDetail.setComment(commentRequest.getComment());
        Comment savedComment = commentRepository.save(commentDetail);

        //db에러 체크
        verificationService.checkDB(savedComment);

        return CommentResponse.response(savedComment);
    }


    //댓글 삭제
    public CommentDeleteResponse deleteComment(int commentId, User user) {
        //댓글이 존재하는지 확인
        Comment commentDetail = verificationService.findCommentById(commentId);

        //유저가 일치하는지 확인
        //ADMIN일경우 스킵
        if(UserRole.USER.equals(user.getRole()))
            verificationService.checkSameUserName(user.getUserName(), commentDetail.getUser().getUserName(),"댓글");

        //댓글 삭제
        commentRepository.delete(commentDetail);

        return CommentDeleteResponse.response("댓글 삭제 완료",commentId);
    }
}
