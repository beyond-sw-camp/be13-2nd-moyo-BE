package com.beyond.backend.domain.project.entity;

import com.beyond.backend.domain.common.BaseEntity;
import com.beyond.backend.domain.feedback.entity.Feedback;
import com.beyond.backend.domain.project.dto.ProjectRequestDto;
import com.beyond.backend.domain.team.entity.Team;
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
@Table(name = "project")
public class Project extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no", nullable = false, unique = true)
    private Long no;

    @Column(nullable = false)
    private String name;

    // 프로젝트 내용/설명
    private String content;

    private int viewCnt;

    // 관계설정 수정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_no")
    private Team team;

    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feedback> feedbacks = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProjectTech> projectTechs = new ArrayList<>();

    //기본 초기화
    @PrePersist
    public void prePersist() {
        if (projectStatus == null) {
            projectStatus = ProjectStatus.OPEN;
        }
    }

    // 조회수 증가
    public void increaseViewCnt() {
        this.viewCnt++;
    }

    //== 연관관계 편의 메서드 ==//
    public void setTeam(Team team){
        this.team = team; // 프로젝트의 팀을 설정
        if (!team.getProjects().contains(this)){
            team.getProjects().add(this);
        }
    }

    public void addFeedbacks(Feedback feedback){
        this.feedbacks.add(feedback);
        feedback.setProject(this);
    }

    public void update(ProjectRequestDto projectRequestDto){
        this.name = projectRequestDto.getName();
        this.content = projectRequestDto.getContent();
        this.projectStatus = projectRequestDto.getProjectStatus();
    }
}