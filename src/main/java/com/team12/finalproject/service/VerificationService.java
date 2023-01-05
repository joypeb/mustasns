package com.team12.finalproject.service;

import com.team12.finalproject.domain.entity.Alarm;
import com.team12.finalproject.domain.entity.Comment;
import com.team12.finalproject.domain.entity.Post;
import com.team12.finalproject.domain.entity.User;
import com.team12.finalproject.domain.role.AlarmType;
import com.team12.finalproject.exception.AppException;
import com.team12.finalproject.exception.ErrorCode;
import com.team12.finalproject.repository.AlarmRepository;
import com.team12.finalproject.repository.CommentRepository;
import com.team12.finalproject.repository.PostRepository;
import com.team12.finalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VerificationService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final AlarmRepository alarmRepository;

    //userName으로 user찾기
    public User findUserByUserName(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(
                () -> new AppException(ErrorCode.USERNAME_NOT_FOUND,String.format("%s는 없는 userName입니다",userName)));
    }

    //id로 user찾기
    public User findUserById(int id) {
        return userRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USERNAME_NOT_FOUND,String.format("%d는 없는 id입니다",id)));
    }

    //userName 중복체크
    public void duplicatedUser(String userName) {
        userRepository.findByUserName(userName)
                .ifPresent(user -> {throw new AppException(ErrorCode.DUPLICATED_USER_NAME, "UserName이 중복됩니다");});
    }

    //post id로 post찾기
    public Post findPostById(int id) {
        Post post =  postRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND,"해당 포스트가 존재하지 않습니다"));
        if(post.getDeletedAt() != null)
            throw new AppException(ErrorCode.POST_NOT_FOUND,"해당 포스트가 존재하지 않습니다");
        return post;
    }

    //post의 deletedAt이 null인지 확인
    public void checkDeleted(LocalDateTime deletedAt) {
        if(deletedAt != null)
            throw new AppException(ErrorCode.POST_NOT_FOUND,"해당 포스트가 존재하지 않습니다");
    }

    //post를 작성한 userName과 현재 userName을 비교
    public void checkSameUserName(String authUserName, String userName, String content) {
        if(!authUserName.equals(userName))
            throw new AppException(ErrorCode.INVALID_PERMISSION, String.format("%s님의 %s이(가) 아닙니다", userName,content));
    }

    //db에러 체크
    public <T> boolean checkDB(T t) {
        if(t == null) throw new AppException(ErrorCode.DATABASE_ERROR, "데이터베이스 에러입니다");
        return true;
    }

    //댓글 존재 확인
    public Comment findCommentById(int id) {
        Comment commentDetail = commentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND,"해당 댓글을 찾을 수 없습니다"));
        if(commentDetail.getDeletedAt() != null)
            throw new AppException(ErrorCode.COMMENT_NOT_FOUND,"해당 댓글을 찾을 수 없습니다");
        return commentDetail;
    }

    //알림 발생
    public void makeAlarm(AlarmType alarmType, Post post, User user) {
        if(post.getUser().getId() != user.getId())
            alarmRepository.save(Alarm.save(alarmType,post,user));
    }

    //알림 찾기
    public Alarm findAlarm(Post post, User user) {
        return alarmRepository.findByTargetIdAndFromUserId(post.getId(),user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.ALARM_NOT_FOUNT,"해당 알림을 찾을 수 없습니다"));
    }

    public void deleteAlarm(Alarm alarm) {
        alarmRepository.delete(alarm);
    }
}
