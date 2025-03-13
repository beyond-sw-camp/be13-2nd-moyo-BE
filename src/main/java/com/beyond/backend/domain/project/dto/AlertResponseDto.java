package com.beyond.backend.domain.project.dto;

import com.beyond.backend.domain.project.entity.Schedule;
import com.beyond.backend.domain.user.entity.User;
import lombok.Data;

@Data
public class AlertResponseDto {

    Long receiverNo;
    Long scheduleNo;
}
