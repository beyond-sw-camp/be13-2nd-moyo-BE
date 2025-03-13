package com.beyond.backend.domain.project.dto;

import com.beyond.backend.domain.project.entity.Schedule;
import com.beyond.backend.domain.project.entity.ScheduleStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@AllArgsConstructor
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
