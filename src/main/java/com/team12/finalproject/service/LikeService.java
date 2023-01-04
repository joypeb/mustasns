package com.team12.finalproject.service;

import com.team12.finalproject.domain.dto.Response;
import com.team12.finalproject.domain.entity.Like;
import com.team12.finalproject.domain.entity.Post;
import com.team12.finalproject.domain.entity.User;
import com.team12.finalproject.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {

    private final LikeRepository likeRepository;
    private final VerificationService verificationService;

    public Response<?> like(int postId, String userName) {
        //글에 대해 좋아요를 했는지 확인
        //이미 좋아요가 되있으면 좋아요를 취소시킨다
        Optional<Like> like = likeRepository.findByPostIdAndUser_UserName(postId,userName);
        User user = verificationService.findUserByUserName(userName);
        Post post = verificationService.findPostById(postId);

        if(like.isEmpty() || like.get().getDeletedAt() != null) {
            Like savedLike = likeRepository.save(Like.save(user, post, null));
            verificationService.checkDB(savedLike);
            return Response.success("좋아요를 눌렀습니다");
        } else if (like.get().getDeletedAt() == null) {
            Like deleteLike = like.get();
            deleteLike.setDeletedAt(LocalDateTime.now());
            Like deletedLike = likeRepository.save(deleteLike);
            verificationService.checkDB(deletedLike);
            return Response.success("좋아요를 취소했습니다");
        }
        return Response.error("에러입니다");
    }

    public Response<?> likeCount(int postId) {
        return Response.success(likeRepository.countByPostIdAndDeletedAtIsNull(postId));
    }
}
