package com.team12.finalproject.domain.dto.userJoin;

import com.team12.finalproject.domain.entity.User;
import com.team12.finalproject.domain.role.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinRequest {
    private String userName;
    private String password;

}
