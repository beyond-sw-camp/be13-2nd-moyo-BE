package com.beyond.backend.domain.team.dto;

import com.beyond.backend.domain.team.entity.Schedule;
import com.beyond.backend.domain.team.entity.ScheduleStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDateTime;

public class ScheduleResponseDto {

    @NotBlank
    private String title;

    @NotBlank
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String description;

    private ScheduleStatus status;

    @Builder
    public ScheduleResponseDto(Schedule schedule) {
        this.title = schedule.getTitle();
        this.startDate = schedule.getStartDate();
        this.endDate = schedule.getEndDate();
        this.description = schedule.getDescription();
        this.status = schedule.getStatus();
    }
}
