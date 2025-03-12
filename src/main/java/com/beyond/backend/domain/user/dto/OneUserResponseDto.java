package com.beyond.backend.domain.user.dto;

import java.time.LocalDateTime;

import com.beyond.backend.domain.common.entity.UserStatus;
import com.beyond.backend.domain.user.entity.UserRoleType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OneUserResponseDto {

	private String username;
	private UserRoleType role;
	private String email;
	private String phoneNum;
	private Boolean banned;
	private int passwordErrorCount;

	private LocalDateTime createdAt;
	private LocalDateTime updateAt;

	private UserStatus status;

}
