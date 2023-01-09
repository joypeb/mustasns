package com.team12.finalproject.service;

import com.team12.finalproject.domain.dto.userJoin.UserJoinRequest;
import com.team12.finalproject.domain.dto.userJoin.UserJoinResponse;
import com.team12.finalproject.domain.dto.userLogin.UserLoginRequest;
import com.team12.finalproject.domain.entity.User;
import com.team12.finalproject.domain.role.UserRole;
import com.team12.finalproject.domain.dto.Response;
import com.team12.finalproject.domain.dto.adminRoleChange.AdminRoleChangeResponse;
import com.team12.finalproject.domain.dto.userLogin.UserLoginResponse;
import com.team12.finalproject.exception.AppException;
import com.team12.finalproject.exception.ErrorCode;
import com.team12.finalproject.repository.UserRepository;
import com.team12.finalproject.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final VerificationService verificationService;

    @Value("${jwt.token.secret}")
    private String secretKey;
    private long expireTimems = 60 * 60 * 1000;

    //user 회원가입
    @Transactional
    public UserJoinResponse join(UserJoinRequest userJoinRequest) {

        //user중복 체크
        verificationService.duplicatedUser(userJoinRequest.getUserName());

        //user를 db에 저장후 db에러 체크
        User user = userRepository.save(User.save(userJoinRequest.getUserName(), encoder.encode(userJoinRequest.getPassword())));
        verificationService.checkDB(user);

        return UserJoinResponse.response(user);
    }

    //user 로그인
    @Transactional
    public UserLoginResponse login(UserLoginRequest userLoginRequest) {
        //유저 아이디 확인
        User user = verificationService.findUserByUserName(userLoginRequest.getUserName());

        //유저 비밀번호 확인
        if(!encoder.matches(userLoginRequest.getPassword(), user.getPassword()))
            throw new AppException(ErrorCode.INVALID_PASSWORD,"패스워드가 틀립니다");

        //jwt발행
        String token = JwtTokenUtils.createToken(userLoginRequest.getUserName(),user.getRole(),secretKey,expireTimems);

        return new UserLoginResponse(token);
    }

    //권한 변경
    @Transactional
    public AdminRoleChangeResponse roleChange(int userId, String role) {
        //user가 존재하는지 확인
        User user = verificationService.findUserById(userId);

        //userRole이 정확히 들어왔는지 확인
        String userRole = role.toUpperCase();
        if(!userRole.equals("USER") && !userRole.equals("ADMIN")) {
            throw new AppException(ErrorCode.INVALID_USERROLE,"userRole이 잘못되었습니다");
        }

        //userRole을 변경한다
        user.setRole(UserRole.valueOf(userRole));
        User changeRoleUser = userRepository.save(user);
        verificationService.checkDB(changeRoleUser);

        return AdminRoleChangeResponse.response(changeRoleUser);
    }

}
