package com.beyond.backend.domain.team.entity;

import com.beyond.backend.domain.common.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Schedule extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    private String title;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private String description;

    @Enumerated(EnumType.STRING)
    private ScheduleStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_no")
    private Team team;

    private boolean isAlertSent;

    @Builder
    public Schedule(String title, LocalDateTime startDate, LocalDateTime endDate, String description, Team team, ScheduleStatus status, Boolean isAlertSent) {
        this.team = team;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.status = status;
        this.isAlertSent = isAlertSent;
    }

    public void updateStatus(ScheduleStatus status){
        this.status = status;
    }

    public void update(String title, String description, LocalDateTime startDate, LocalDateTime endDate, ScheduleStatus status) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public void updateAlertSent(boolean isAlertSent) {
        this.isAlertSent = isAlertSent;
    }
}