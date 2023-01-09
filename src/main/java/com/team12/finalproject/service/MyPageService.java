package com.team12.finalproject.service;

import com.team12.finalproject.domain.dto.Response;
import com.team12.finalproject.domain.dto.myFeed.MyFeedResponse;
import com.team12.finalproject.domain.entity.User;
import com.team12.finalproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final PostRepository postRepository;

    public Page<MyFeedResponse> myFeed(User user, Pageable pageable) {
        return MyFeedResponse.pageList(postRepository.findAllByUser_UserName(user.getUserName(), pageable));
    }
}
