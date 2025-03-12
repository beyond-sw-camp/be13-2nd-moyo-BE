package com.beyond.backend.domain.project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleRequestDto {

    Long projectId;

    @NotBlank
    private String title;

    @NotBlank
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String description;

}
