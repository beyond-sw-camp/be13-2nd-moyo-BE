package com.beyond.backend.domain.user.dto;

import lombok.Data;

@Data
public class BanRequestDto {

    String username;
    Boolean ban;
}