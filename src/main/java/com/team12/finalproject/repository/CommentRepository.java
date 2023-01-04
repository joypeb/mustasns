package com.team12.finalproject.repository;

import com.team12.finalproject.domain.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Page<Comment> findAllByPostIdAndDeletedAtIsNull(int postId, Pageable pageable);
}
