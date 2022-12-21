package com.team12.finalproject.service;

import com.team12.finalproject.domain.User;
import com.team12.finalproject.domain.UserRole;
import com.team12.finalproject.domain.dto.Response;
import com.team12.finalproject.domain.dto.userJoin.UserJoinRequest;
import com.team12.finalproject.domain.dto.userJoin.UserJoinResponse;
import com.team12.finalproject.domain.dto.userJoin.UserJoinResult;
import com.team12.finalproject.domain.dto.userLogin.UserLoginRequest;
import com.team12.finalproject.exception.AppException;
import com.team12.finalproject.exception.ErrorCode;
import com.team12.finalproject.repository.UserRepository;
import com.team12.finalproject.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.team12.finalproject.domain.dto.userJoin.UserJoinRequest.toEntity;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.token.secret}")
    private String secretKey;
    private long expireTimems = 60 * 60 * 1000;

    //user 회원가입
    public Response<UserJoinResult> join(UserJoinRequest userJoinRequest) {

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
        Response userJoinResponse = Response.builder().result("SUCCESS").result(new UserJoinResult(user.getId(),user.getUserName())).build();
        return userJoinResponse;
    }

    //user 로그인
    public String login(UserLoginRequest userLoginRequest) {
        //유저 아이디 확인
        User user = userRepository.findByUserName(userLoginRequest.getUserName()).orElseThrow(
                () -> new AppException(ErrorCode.USERNAME_NOT_FOUND,String.format("%d는 없는 userName입니다",userLoginRequest.getUserName()))
        );

        //유저 비밀번호 확인
        boolean passwordMatch = encoder.matches(userLoginRequest.getPassword(), user.getPassword());
        if(!passwordMatch) throw new AppException(ErrorCode.INVALID_PASSWORD,"패스워드가 틀립니다");

        //jwt발행
        return JwtTokenUtils.createToken(user.getUserName(),secretKey,expireTimems);
    }
}
