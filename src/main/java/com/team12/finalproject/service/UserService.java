package com.team12.finalproject.service;

import com.team12.finalproject.domain.User;
import com.team12.finalproject.domain.UserRole;
import com.team12.finalproject.domain.dto.Response;
import com.team12.finalproject.domain.dto.adminRoleChange.AdminRoleChangeResponse;
import com.team12.finalproject.domain.dto.userJoin.UserJoinResult;
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

import java.util.stream.Stream;

import static com.team12.finalproject.domain.dto.userJoin.UserJoinRequest.toEntity;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final FindUser findUser;

    @Value("${jwt.token.secret}")
    private String secretKey;
    private long expireTimems = 60 * 60 * 1000;
    private long refreshExpireTimems = 60 * 60 * 10000;

    //user 회원가입
    @Transactional
    public Response<UserJoinResult> join(String userName, String password) {

        //user중복 체크
        userRepository.findByUserName(userName)
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.DUPLICATED_USER_NAME, "UserName이 중복됩니다");
                });


        //user를 db에 저장
        User user = userRepository.save(toEntity(userName, encoder.encode(password),UserRole.USER));
        if(user.getId() < 1) throw new AppException(ErrorCode.DATABASE_ERROR, "데이터베이스 에러 발생");

        //response 작성
        UserJoinResult userJoinResult = new UserJoinResult(user.getId(), user.getUserName());
        return Response.success(userJoinResult);
    }

    //user 로그인
    @Transactional
    public Response<UserLoginResponse> login(String userName, String password) {
        //유저 아이디 확인
        User user = findUser.findUserByUserName(userName);

        //유저 비밀번호 확인
        boolean passwordMatch = encoder.matches(password, user.getPassword());
        if(!passwordMatch) throw new AppException(ErrorCode.INVALID_PASSWORD,"패스워드가 틀립니다");

        //jwt발행
        String token = JwtTokenUtils.createToken(user.getUserName(),secretKey,expireTimems);
        //String refreshToken = JwtTokenUtils.createRefreshToken(user.getUserName(),secretKey,refreshExpireTimems);

        //refreshToken을 db에 저장
        /*user.setRefreshToken(refreshToken);
        userRepository.save(user);*/

        /*Map<String,String> tokenMap = new HashMap<>();
        tokenMap.put("token",token);
        tokenMap.put("refreshToken",refreshToken);*/

        UserLoginResponse userLoginResponse = new UserLoginResponse(token);

        return Response.success(userLoginResponse);
    }

    //권한 변경
    @Transactional
    public Response<AdminRoleChangeResponse> roleChange(Integer id, String role) {
        //user가 존재하는지 확인
        User user = userRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USERNAME_NOT_FOUND,String.format("%d는 없는 id입니다",id))
        );

        //userRole이 정확히 들어왔는지 확인
        String userRole = role.toUpperCase();
        if(!userRole.equals("USER") && !userRole.equals("ADMIN")) {
            throw new AppException(ErrorCode.INVALID_USERROLE,"userRole이 잘못되었습니다");
        }

        //userRole을 변경한다
        user.setRole(UserRole.valueOf(userRole));
        User changeRoleUser = userRepository.save(user);

        //db에 저장이 잘 되었는지 확인한다
        if(changeRoleUser.getUserName().equals("") || changeRoleUser.getUserName().equals(null)) {
            throw new AppException(ErrorCode.DATABASE_ERROR,"DB에러입니다");
        }

        AdminRoleChangeResponse adminRoleChangeResponse =
                new AdminRoleChangeResponse(String.format("userRole이 %s로 변경되었습니다", String.valueOf(changeRoleUser.getRole())), changeRoleUser.getId());

        return Response.success(adminRoleChangeResponse);
    }

}
