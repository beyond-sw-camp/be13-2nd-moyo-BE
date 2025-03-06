package com.beyond.backend.domain.project.entity;

import java.util.ArrayList;
import java.util.List;

import com.beyond.backend.domain.common.BaseEntity;
import com.beyond.backend.domain.feedback.entity.Feedback;
import com.beyond.backend.domain.team.entity.Team;
import com.beyond.backend.domain.tech.entity.Tech;
import com.beyond.backend.domain.project.dto.ProjectRequestDto;

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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "project_no")// 단방향
    private List<ProjectTech> projectTeches;


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

    // project Tech 연관관계 메서드 지정
    public void addProjectTech(List<ProjectTech> list){
        if (this.projectTeches == null) {
            this.projectTeches = new ArrayList<>();
        }
        this.projectTeches.clear(); // 기존 리스트 초기화
        this.projectTeches.addAll(projectTeches); // 새로운 리스트 추가
    }

    // 프로젝트 제목, 내용, 상태만 변경
    public void update(ProjectRequestDto projectRequestDto){
        this.name = projectRequestDto.getName();
        this.content = projectRequestDto.getContent();
        this.projectStatus = projectRequestDto.getProjectStatus();
    }
}