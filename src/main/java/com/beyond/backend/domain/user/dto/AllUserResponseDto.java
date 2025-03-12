package com.beyond.backend.domain.user.dto;

import com.beyond.backend.domain.user.entity.UserRoleType;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AllUserResponseDto {

    private String username;
    private UserRoleType role;
    private String email;
    private Boolean banned;

}
