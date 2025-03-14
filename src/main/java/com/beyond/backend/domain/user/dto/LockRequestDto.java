package com.beyond.backend.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

public class LockRequestDto {
    @NotBlank
    String username;
}