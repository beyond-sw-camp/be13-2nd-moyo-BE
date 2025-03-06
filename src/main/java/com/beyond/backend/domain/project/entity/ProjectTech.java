package com.beyond.backend.domain.project.entity;

import com.beyond.backend.domain.tech.entity.Tech;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "project_tech", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"project_no", "tech_no"})
})
public class ProjectTech {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_no", nullable = false)
    private Long projectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tech_no", nullable = false)
    private Tech tech;


    //== 연관관계 편의 메서드 ==//
    // public void setProject(Project project){
    //     this.project = project;
    //     project.getProjectTeches().add(this);
    // }
/*    public ProjectTech(Tech tech, Long projectNo){
        this.tech = tech;
        this.projectId =No projectNo;
    }*/

}


// tech = 모든 기술들
// project tech = 이 프로젝트에서 사용하는 기술들
