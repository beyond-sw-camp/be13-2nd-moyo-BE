package com.beyond.backend.domain.project.dto;

import com.beyond.backend.domain.project.entity.ProjectTech;
import com.beyond.backend.domain.tech.entity.Tech;
import lombok.Data;

@Data
public class ProjectTechResponseDto {

    private Tech tech;
    private Long projectNo;

    public ProjectTechResponseDto(ProjectTech projectTech) {
        this.tech = projectTech.getTech();
        this.projectNo = projectNo;
    }
}
