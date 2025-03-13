package com.beyond.backend.domain.project.entity;

import java.util.ArrayList;
import java.util.List;

import com.beyond.backend.domain.common.BaseEntity;
import com.beyond.backend.domain.feedback.entity.Feedback;
import com.beyond.backend.domain.project.dto.ProjectUpdateRequestDto;
import com.beyond.backend.domain.team.entity.Team;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
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

    private int viewCnt = 0;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_no")
    private Team team;

    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feedback> feedbacks = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectTech> projectTeches = new ArrayList<>();


    @Builder
    public Project(String name, String content, Team team) {
        this.name = name;
        this.content = content;
        this.team = team;
        this.projectStatus = ProjectStatus.OPEN;
    }


    public void addProjectTechList(List<ProjectTech> teches){
        this.projectTeches = teches;
    }


    public void update(ProjectStatus projectStatus, ProjectUpdateRequestDto projectRequestDto){
        this.name = projectRequestDto.getName();
        this.content = projectRequestDto.getContent();
        this.projectStatus = projectStatus;
    }
}
