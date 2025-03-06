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

    @Column(name = "project_no", nullable = false)
    private Long projectNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tech_no", nullable = false)
    private Tech tech;



    @Builder
    public ProjectTech(Tech tech, Long projectNo){
        this.tech = tech;
        this.projectNo = projectNo;
    }
}
