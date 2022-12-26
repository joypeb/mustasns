package com.team12.finalproject.fixture;

import com.team12.finalproject.domain.Post;

public class PostFixture {
    public static Post get(String userName, String password) {
        Post postEntity = Post.builder()
                .id(1)
                .user(UserFixture.get(userName, password))
                .title("title")
                .body("body")
                .build();
        return postEntity;
    }

}
