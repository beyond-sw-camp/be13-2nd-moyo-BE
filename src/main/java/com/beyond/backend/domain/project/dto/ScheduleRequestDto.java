package com.beyond.backend.domain.project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

import com.beyond.backend.domain.project.entity.ScheduleStatus;

@Data
public class ScheduleRequestDto {

    Long teamNo;

    private String title;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String description;

    private ScheduleStatus status;


}
