package com.team12.finalproject.service;

import com.team12.finalproject.domain.dto.Response;
import com.team12.finalproject.domain.entity.Like;
import com.team12.finalproject.domain.entity.Post;
import com.team12.finalproject.domain.entity.User;
import com.team12.finalproject.exception.AppException;
import com.team12.finalproject.exception.ErrorCode;
import com.team12.finalproject.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class LikeService {

    private final LikeRepository likeRepository;
    private final VerificationService verificationService;

    @Transactional
    public String like(int postId, String userName) {
        String likeString = "좋아요를 눌렀습니다";
        String unlikeString = "좋아요를 취소했습니다";
        String error = "에러입니다";

        //글에 대해 좋아요를 했는지 확인
        //이미 좋아요가 되있으면 좋아요를 취소시킨다
        User user = verificationService.findUserByUserName(userName);
        Post post = verificationService.findPostById(postId);
        Optional<Like> like = likeRepository.findByPostAndUser(post,user);

        log.info("like 확인 : " + like.get().getId());

        if(like.isEmpty()) {
            Like savedLike = likeRepository.save(Like.save(user,post,null));

            verificationService.checkDB(savedLike);

            return likeString;
        }
        else if(like.get().getDeletedAt() != null) {
            int result = likeRepository.updateByLikeId(like.get().getId());

            if(result < 1)
                throw new AppException(ErrorCode.DATABASE_ERROR,"DB에러입니다");

            return likeString;
        }
        else if (like.get().getDeletedAt() == null) {
            likeRepository.delete(like.get());

            return unlikeString;
        }
        return error;
    }

    public int likeCount(int postId) {
        return likeRepository.countByPostId(postId);
    }
}
