package com.beyond.backend.data.entity;

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
public class Team extends BaseEntity{

    @Id
    @GeneratedValue
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
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> projects = new ArrayList<>();

    public void updateTeamDetails(String teamName, String teamIntroduce, ProjectStatus projectStatus) {
        this.teamName = teamName;
        this.teamIntroduce = teamIntroduce;
        this.projectStatus = projectStatus;

    }
}
