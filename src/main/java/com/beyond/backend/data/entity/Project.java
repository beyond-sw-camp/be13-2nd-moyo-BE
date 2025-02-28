package com.beyond.backend.data.entity;

import java.util.ArrayList;
import java.util.List;

import com.beyond.backend.data.dto.ProjectDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
import jakarta.persistence.OneToOne;
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

    private String content;

    // 관계설정 수정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_no")
    private Team team;

    private String projectPurpose;

    private String projectSubject;

    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedBack> feedBacks = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectTech> projectTeches;



    // User 수
    public int countUser(Team team){
        return team.getTeamUsers().size();
    }

    public void update(ProjectDto projectDto){
        this.name = projectDto.getName();
        this.content = projectDto.getContent();
        this.projectPurpose = projectDto.getProjectPurpose();
        this.projectSubject = projectDto.getProjectSubject();
        // 팀을 변경 ?
        this.projectStatus = projectDto.getProjectStatus();
    }

}
