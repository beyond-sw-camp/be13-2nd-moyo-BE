package com.beyond.backend.domain.team.dto;

import com.beyond.backend.domain.team.entity.Schedule;
import com.beyond.backend.domain.team.entity.ScheduleStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ScheduleResponseDto {

    private String title;

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
