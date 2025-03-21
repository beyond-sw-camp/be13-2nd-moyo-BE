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
 * 2025-03-21        hongjm           더 많은 정보를 반환하게 수정
 */

import com.beyond.backend.domain.project.dto.ProjectRequestDto;
import com.beyond.backend.domain.project.dto.ProjectResponseDto;
import com.beyond.backend.domain.project.entity.ProjectStatus;
import lombok.Data;

import java.util.List;

@Data
public class TeamDetailDto {

    private TeamResponseDto team;

    private ProjectRequestDto project;

    public TeamDetailDto(Long teamNo, String teamName, String teamIntroduce, ProjectStatus teamStatus,
                         String name, String content, Long projectNo, ProjectStatus projectStatus) {
        this.team = new TeamResponseDto(teamNo, teamName, teamIntroduce, teamStatus);
        this.project = new ProjectRequestDto(name, content, projectNo, projectStatus);
    }
}
