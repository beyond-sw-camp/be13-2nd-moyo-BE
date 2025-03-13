package com.beyond.backend.domain.team.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleUpdateRequestDto {
    @NotBlank
    private String title;

    @NotBlank
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String description;
}

