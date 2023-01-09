package com.team12.finalproject.service;

import com.team12.finalproject.domain.dto.Response;
import com.team12.finalproject.domain.entity.Alarm;
import com.team12.finalproject.domain.entity.Like;
import com.team12.finalproject.domain.entity.Post;
import com.team12.finalproject.domain.entity.User;
import com.team12.finalproject.domain.role.AlarmType;
import com.team12.finalproject.exception.AppException;
import com.team12.finalproject.exception.ErrorCode;
import com.team12.finalproject.repository.AlarmRepository;
import com.team12.finalproject.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class LikeService {

    private final LikeRepository likeRepository;
    private final VerificationService verificationService;

    @Transactional
    public String like(int postId, User user) {
        String likeString = "좋아요를 눌렀습니다";
        String unlikeString = "좋아요를 취소했습니다";
        String error = "에러입니다";

        //글에 대해 좋아요를 했는지 확인
        //이미 좋아요가 되있으면 좋아요를 취소시킨다
        User findUser = verificationService.findUserByUserName(user.getUserName());
        Post post = verificationService.findPostById(postId);
        Optional<Like> like = likeRepository.findByPostAndUser(post,findUser);

        if(like.isEmpty()) {
            //좋아요
            Like savedLike = likeRepository.save(Like.save(findUser,post,null));
            verificationService.checkDB(savedLike);

            //알림 발생
            verificationService.makeAlarm(AlarmType.NEW_LIKE_ON_POST,post,findUser);

            return likeString;
        }
        else if(like.get().getDeletedAt() != null) {
            //좋아요
            int result = likeRepository.updateByLikeId(like.get().getId());
            if(result < 1)
                throw new AppException(ErrorCode.DATABASE_ERROR,"DB에러입니다");

            //알림 발생
            verificationService.makeAlarm(AlarmType.NEW_LIKE_ON_POST,post,findUser);

            return likeString;
        }
        else if (like.get().getDeletedAt() == null) {
            likeRepository.delete(like.get());

            //알림 제거
            verificationService.deleteAlarm(verificationService.findAlarm(post,findUser));

            return unlikeString;
        }
        return error;
    }

    public int likeCount(int postId) {
        return likeRepository.countByPostId(postId);
    }
}
