package com.team12.finalproject.service;

import com.team12.finalproject.domain.User;
import com.team12.finalproject.exception.AppException;
import com.team12.finalproject.exception.ErrorCode;
import com.team12.finalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindUser {

    private final UserRepository userRepository;

    public User findUserByUserName(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(
                () -> new AppException(ErrorCode.USERNAME_NOT_FOUND,String.format("%s는 없는 userName입니다",userName)));
    }
}
