package com.beyond.backend.domain.team.dto;

import com.beyond.backend.domain.project.entity.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *
 * <p>packageName    : com.beyond.backend.data.dto.teamDto
 * <p>fileName       : TeamResponseDto
 * <p>author         : hongjm
 * <p>date           : 2025-02-03
 * <p>description    : 팀 상세 Dto
 */
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-03        hongjm           최초 생성
 * 2025-02-16        hongjm           Entity에 맞춰 수정
 * 2025-02-23        hongjm           no 항목 추가
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamResponseDto {

    private long no;

    TeamDto team;

    public TeamResponseDto(Long no, String teamName, String teamIntroduce, ProjectStatus projectStatus) {
        this.no = no;
        this.team = new TeamDto(teamName, teamIntroduce, projectStatus);
    }
}
