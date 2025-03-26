package com.beyond.backend.domain.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class MatchEmailRequestDto {

    @Email
    String email;

    String username;

}
