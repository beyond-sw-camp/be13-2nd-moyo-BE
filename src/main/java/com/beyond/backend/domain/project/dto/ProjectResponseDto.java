package com.beyond.backend.domain.project.dto;

import java.util.List;
import com.beyond.backend.domain.project.entity.Project;
import com.beyond.backend.domain.project.entity.ProjectStatus;
import com.beyond.backend.domain.project.entity.ProjectTech;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponseDto {

    private Long no;

    private String name;

    private String teamName;

    private String content;

    private ProjectStatus projectStatus;

    private List<String> projectTeches;

    private int view;

    public ProjectResponseDto(Long no, String name, String teamName, String content, int view,  ProjectStatus projectStatus) {
        this.no = no;
        this.name = name;
        this.teamName = teamName;
        this.content = content;
        this.view = view;
        this.projectStatus = projectStatus;
    }

    public ProjectResponseDto(Project project) {
        this.no = project.getNo();
        this.name = project.getName();
        this.teamName = project.getTeam().getTeamName();
        this.content = project.getContent();
        this.view = project.getViewCnt();
        this.projectStatus = project.getProjectStatus();
        this.projectTeches = project.getProjectTeches().stream()
            .map(pt -> pt.getTech().getTechName()) // Tech 엔티티에서 techName 필드 추출
            .toList();
    }

}
