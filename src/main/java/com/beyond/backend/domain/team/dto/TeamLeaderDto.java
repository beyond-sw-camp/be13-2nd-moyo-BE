package com.beyond.backend.domain.team.dto;

import lombok.Builder;
import lombok.Data;

/**
 * <p>
 *
 * <p>packageName    : com.beyond.backend.domain.team.dto.TeamLeaderDto
 * <p>fileName       : TeamLeaderDto
 * <p>author         : hongjm
 * <p>date           : 2025-03-23
 * <p>description    : 팀장 정보 Dto
 */
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-23        hongjm           최초 생성
 */

@Data
@Builder
public class TeamLeaderDto {
    private Boolean Leader;

    private Long TeamNo;

    private Long ProjectNo;
}
