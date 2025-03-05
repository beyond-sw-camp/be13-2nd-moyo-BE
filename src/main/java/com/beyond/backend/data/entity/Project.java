package com.beyond.backend.data.entity;

import java.util.ArrayList;
import java.util.List;

import com.beyond.backend.data.dto.ProjectRequestDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <p>
 *
 * <p>packageName    : com.beyond.backend.data.entity
 * <p>fileName       : Project
 * <p>author         : jaewoo
 * <p>date           : 2025. 2. 1.
 * <p>description    : 프로젝트 Entity
 */
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025. 2. 1.        jaewoo             최초 생성
 * 2025. 2. 3.        jaewoo             변수명 수정
 * 2025. 2. 4.        jaewoo             변수명 수정
 */

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

    // 관계설정 수정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_no")
    private Team team;

    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feedback> feedbacks = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "project_no")// 단방향
    private List<ProjectTech> projectTeches;

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

    // project Tech 연관관계 메서드 지정
    public void addTech(Tech tech){
        ProjectTech projectTech = new ProjectTech(tech, this.no);
        projectTeches.add(projectTech);
    }


    // 프로젝트 제목, 내용, 상태만 변경
    public void update(ProjectRequestDto projectRequestDto){
        this.name = projectRequestDto.getName();
        this.content = projectRequestDto.getContent();
        this.projectStatus = projectRequestDto.getProjectStatus();
    }
}
