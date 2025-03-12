package com.beyond.backend.domain.user.dto;

import lombok.Data;

@Data
public class LockResponseDto {

    String message = "";

    public LockResponseDto() {
        this.message = "user Locked";
    }
}