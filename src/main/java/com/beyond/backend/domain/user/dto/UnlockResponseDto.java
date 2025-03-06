package com.beyond.backend.domain.user.dto;

import lombok.Data;

@Data
public class UnlockResponseDto {
    String message;

    public UnlockResponseDto() {
        this.message = "unlocked";
    }

}
