package com.beyond.backend.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {

    @NotBlank(message="유저 이름을 입력해 주십시오.")
    private String username;

    @NotBlank(message="비밀번호를 입력해 주십시오.")
    private String password;
}