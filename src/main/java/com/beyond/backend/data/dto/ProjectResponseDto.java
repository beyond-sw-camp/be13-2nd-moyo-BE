package com.beyond.backend.data.dto;

import java.util.List;

import com.beyond.backend.data.entity.Project;
import com.beyond.backend.data.entity.ProjectStatus;
import com.beyond.backend.data.entity.ProjectTech;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *
 * <p>packageName    : com.beyond.backend.data.dto
 * <p>fileName       : ProjectResponseDto
 * <p>author         : jaewoo
 * <p>date           : 2025. 2. 2.
 * <p>description    : 프로젝트 응답 Dto
 */


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

    public ProjectResponseDto(Long no, String name, String teamName, String content, ProjectStatus projectStatus) {
        this.no = no;
        this.name = name;
        this.teamName = teamName;
        this.content = content;
        this.projectStatus = projectStatus;
        this.projectTeches = List.of(); // 기본값 설정
    }

    public void setProjectTeches(List<String> projectTeches) {
        this.projectTeches = projectTeches;
    }

    public ProjectResponseDto(Project project) {
        this.no = project.getNo();
        this.name = project.getName();
        this.teamName = project.getTeam().getTeamName();
        this.content = project.getContent();
        this.projectStatus = project.getProjectStatus();

    }



}
