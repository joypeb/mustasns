package com.team12.finalproject.service;

import com.team12.finalproject.domain.User;
import com.team12.finalproject.domain.UserRole;
import com.team12.finalproject.domain.dto.userJoin.UserJoinRequest;
import com.team12.finalproject.domain.dto.userJoin.UserJoinResponse;
import com.team12.finalproject.domain.dto.userJoin.UserJoinResult;
import com.team12.finalproject.domain.dto.userLogin.UserLoginRequest;
import com.team12.finalproject.exception.AppException;
import com.team12.finalproject.exception.ErrorCode;
import com.team12.finalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.team12.finalproject.domain.dto.userJoin.UserJoinRequest.toEntity;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    //user 회원가입
    public UserJoinResponse join(UserJoinRequest userJoinRequest) {

        //user중복 체크
        userRepository.findByUserName(userJoinRequest.getUserName())
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.DUPLICATED_USER_NAME, "UserName이 중복됩니다");
                });

        log.info("service " + userJoinRequest.getUserName());
        //user를 db에 저장
        User user = userRepository.save(toEntity(userJoinRequest, encoder.encode(userJoinRequest.getPassword()),UserRole.USER));
        if(user.getId() < 1) throw new AppException(ErrorCode.DATABASE_ERROR, "데이터베이스 에러 발생");

        //response 작성
        return UserJoinResponse.builder()
                .resultCode("SUCCESS")
                .result(new UserJoinResult(user.getId(),user.getUserName()))
                .build();
    }

    //user 로그인
    public String login(UserLoginRequest userLoginRequest) {

        return "";
    }
}
