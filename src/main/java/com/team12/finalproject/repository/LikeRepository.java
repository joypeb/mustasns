package com.team12.finalproject.repository;

import com.team12.finalproject.domain.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    Optional<Like> findByPostIdAndUser_UserName(int postId, String userName);
    Integer countByPostIdAndDeletedAtIsNull(int postId);
}
