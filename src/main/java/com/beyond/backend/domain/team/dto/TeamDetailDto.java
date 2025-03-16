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

import com.beyond.backend.domain.project.dto.ProjectRequestDto;

public class TeamDetailDto {
    private TeamDto team;

    private ProjectRequestDto project;
}
