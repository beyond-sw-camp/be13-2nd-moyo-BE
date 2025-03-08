package com.beyond.backend.domain.project.entity;

import com.beyond.backend.domain.tech.entity.Tech;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_no", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tech_no", nullable = false)
    private Tech tech;

    @Builder
    public ProjectTech(Tech tech, Project project){
        this.tech = tech;
        this.project = project;
    }

}
