package com.team12.finalproject.fixture;

import com.team12.finalproject.domain.entity.User;
import com.team12.finalproject.domain.role.UserRole;

import java.time.LocalDateTime;

public class UserFixture {
    public static User get(String userName, String password) {
        User entity = new User();
        entity.setId(1);
        entity.setUserName(userName);
        entity.setPassword(password);
        entity.setRole(UserRole.USER);
        entity.setCreatedAt(LocalDateTime.now());
        return entity;
    }
}