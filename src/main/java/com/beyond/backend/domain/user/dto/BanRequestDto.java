package com.beyond.backend.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BanRequestDto {

    @NotBlank
    String username;
    Boolean ban;
}