package com.beyond.backend.domain.team.entity;

import com.beyond.backend.domain.common.BaseEntity;
import com.beyond.backend.domain.project.entity.Schedule;
import com.beyond.backend.domain.teamUser.entity.TeamUser;
import com.beyond.backend.domain.project.entity.Project;
import com.beyond.backend.domain.project.entity.ProjectStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(nullable = false)
    private String teamName;

    @Column
    private String teamIntroduce;

    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;

    
    // [홍재민] 25-02-20 팀-유저 중간테이블 cascade 설정
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamUser> teamUsers = new ArrayList<>();

    // [홍도현] 25-02-26 팀-프로젝트 관계 수정
    @OneToOne(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private Project project;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> schedules = new ArrayList<>();

    // [홍도현] 25-03-02 연관관계 편의 메서드
    public void setProject(Project project){
        this.project = project;
        project.setTeam(this);
    }

    public void updateTeamDetails(String teamName, String teamIntroduce, ProjectStatus projectStatus) {
        this.teamName = teamName;
        this.teamIntroduce = teamIntroduce;
        this.projectStatus = projectStatus;
    }

    public void addSchedule(Schedule schedule) {
        this.schedules.add(schedule);
    }
}