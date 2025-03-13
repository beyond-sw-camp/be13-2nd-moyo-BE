package com.beyond.backend.domain.team.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertResponseDto {

    Long receiverNo;
    Long scheduleNo;
}