package com.beyond.backend.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class JoinRequestDto {

    @NotBlank
    @Size(min = 2, max = 20)
    private String username;

    @NotBlank
    @Size(min = 2, max = 20)
    private String password;

    @NotBlank
    @Size(min = 2, max = 20)
    private String confirmPassword;

    @NotBlank
    @Email
    private String email;
}