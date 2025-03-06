package com.beyond.backend.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordUpdateRequestDto {

    @NotBlank
    String username;

    @NotBlank
    @Size(min = 2, max = 20)
    String currentPassword;

    @NotBlank
    @Size(min = 2, max = 20)
    String newPassword;

    @NotBlank
    @Size(min = 2, max = 20)
    String confirmPassword;

}