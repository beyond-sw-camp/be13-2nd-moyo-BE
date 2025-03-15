package com.beyond.backend.domain.user.dto;

import lombok.Data;

@Data
public class DeleteUserByAdminResponseDto {

    String message = "";
    
    public DeleteUserByAdminResponseDto() {

    }

    public DeleteUserByAdminResponseDto(String message) {
        this.message = message;
    }
}