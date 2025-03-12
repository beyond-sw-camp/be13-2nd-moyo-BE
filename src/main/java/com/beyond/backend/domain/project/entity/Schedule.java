package com.beyond.backend.domain.project.entity;

import com.beyond.backend.domain.common.Base;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Schedule extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private String description;

    @Enumerated(EnumType.STRING)
    private ScheduleStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Builder
    public Schedule(String title, LocalDateTime startDate, LocalDateTime endDate, String description) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.status = ScheduleStatus.PENDING;
    }

    public void updateStatus(ScheduleStatus status){
        this.status = status;
    }

    public void update(String title, String description, LocalDateTime startDate, LocalDateTime endDate) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
