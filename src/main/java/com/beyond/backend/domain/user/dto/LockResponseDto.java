package com.beyond.backend.domain.user.dto;

@Data
public class LockResponseDto {

    String message = "";

    public LockResponseDto() {
        this.message = "user Locked";
    }

}
