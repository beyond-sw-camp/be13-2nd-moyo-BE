package com.beyond.backend.domain.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailAuthResponseDto {
    private boolean success;
    private String responseMessage;

}
