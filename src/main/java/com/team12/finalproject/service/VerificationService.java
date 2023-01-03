package com.team12.finalproject.service;

import com.team12.finalproject.domain.entity.Post;
import com.team12.finalproject.domain.entity.User;
import com.team12.finalproject.exception.AppException;
import com.team12.finalproject.exception.ErrorCode;
import com.team12.finalproject.repository.PostRepository;
import com.team12.finalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VerificationService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    //userName으로 user찾기
    public User findUserByUserName(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(
                () -> new AppException(ErrorCode.USERNAME_NOT_FOUND,String.format("%s는 없는 userName입니다",userName)));
    }

    //id로 user찾기
    public User findUserById(int id) {
        return userRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USERNAME_NOT_FOUND,String.format("%d는 없는 id입니다",id)));
    }

    //userName 중복체크
    public void duplicatedUser(String userName) {
        userRepository.findByUserName(userName)
                .ifPresent(user -> {throw new AppException(ErrorCode.DUPLICATED_USER_NAME, "UserName이 중복됩니다");});
    }

    //post id로 post찾기
    public Post findPostById(int id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND,"해당 포스트가 존재하지 않습니다"));
    }

    //post를 작성한 userName과 현재 userName을 비교
    public void checkSameUserName(String postUserName, String userName) {
        if(!postUserName.equals(userName))
            throw new AppException(ErrorCode.INVALID_PERMISSION, String.format("%s님의 포스트가 아닙니다", userName));
    }

    //db에러 체크
    public <T> boolean checkDB(T t) {
        if(t == null) throw new AppException(ErrorCode.DATABASE_ERROR, "데이터베이스 에러입니다");
        return true;
    }
}
