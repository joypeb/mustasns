package com.team12.finalproject.repository;

import com.team12.finalproject.domain.entity.Like;
import com.team12.finalproject.domain.entity.Post;
import com.team12.finalproject.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    Optional<Like> findByPostAndUser(Post post, User user);
    Integer countByPostId(int postId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Like l SET l.deletedAt = NULL WHERE l.id = :id")
    int updateByLikeId(@Param(value="id") int id);
}
