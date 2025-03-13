package com.beyond.backend.domain.team.dto;

import com.beyond.backend.domain.team.entity.ScheduleStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleRequestDto {

    private Long teamNo;

    private String title;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String description;

    private ScheduleStatus status;

}
