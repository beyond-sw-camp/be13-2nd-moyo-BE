package com.beyond.backend.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateRequestDto {

    @NotBlank
    @Size(min = 2, max = 20)
    private String username;

    @Email
    @NotBlank
    @Size(min = 2, max = 20)
    private String email;
}
