package com.beyond.backend.domain.team.dto;

/**
 * <p>
 *
 * <p>packageName    : com.beyond.backend.domain.team.dto.TeamDetailDto
 * <p>fileName       : TeamDetailDto
 * <p>author         : hongjm
 * <p>date           : 2025-02-03
 * <p>description    : 팀 상세 Dto
 */
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-16        hongjm           최초 생성
 */

import com.beyond.backend.domain.project.entity.ProjectStatus;
import lombok.Data;

@Data
public class TeamDetailDto {
    private String teamName;

    private String teamIntroduce;

    private ProjectStatus projectStatus;

    private String name;

    private String content;

    public TeamDetailDto(String teamName, String teamIntroduce,  String name, String content, ProjectStatus projectStatus) {
        this.teamName = teamName;
        this.teamIntroduce = teamIntroduce;
        this.name = name;
        this.content = content;
        this.projectStatus = projectStatus;
    }
}
