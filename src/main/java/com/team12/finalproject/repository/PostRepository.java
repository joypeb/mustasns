package com.team12.finalproject.repository;

import com.team12.finalproject.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {

    Page<Post> findAll(Pageable pageable);
    Page<Post> findAllByUser_UserName(String userName, Pageable pageable);
}
