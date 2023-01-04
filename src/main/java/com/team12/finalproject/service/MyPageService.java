package com.team12.finalproject.service;

import com.team12.finalproject.domain.dto.Response;
import com.team12.finalproject.domain.dto.myFeed.MyFeedResponse;
import com.team12.finalproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final PostRepository postRepository;

    public Response<?> myFeed(String userName, Pageable pageable) {
        return Response.success(MyFeedResponse.pageList(postRepository.findAllByUser_UserName(userName, pageable)));
    }
}
