package com.team12.finalproject.domain.dto.adminRoleChange;

import com.team12.finalproject.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminRoleChangeRequest {
    private UserRole role;
}
