package com.beyond.backend.domain.team.dto;

import com.beyond.backend.domain.team.entity.Schedule;
import com.beyond.backend.domain.team.entity.ScheduleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ScheduleResponseDto {

    private Long scheduleNo;

    private String title;

    private Long createdBy;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String description;

    private ScheduleStatus status;

    public ScheduleResponseDto(Long scheduleNo, String title, LocalDateTime startDate, LocalDateTime endDate, String description, ScheduleStatus status, Long createdBy) {
        this.scheduleNo = scheduleNo;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.status = status;
        this.createdBy = createdBy;
    }

    @Builder
    public ScheduleResponseDto(Schedule schedule) {
        this.scheduleNo = schedule.getNo();
        this.title = schedule.getTitle();
        this.startDate = schedule.getStartDate();
        this.endDate = schedule.getEndDate();
        this.description = schedule.getDescription();
        this.status = schedule.getStatus();
        this.createdBy = schedule.getCreatedBy();
    }
}
