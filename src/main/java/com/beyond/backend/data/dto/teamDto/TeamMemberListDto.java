package com.beyond.backend.data.dto.teamDto;

import com.beyond.backend.data.entity.TeamJoinStatus;
import lombok.Builder;
import lombok.Data;

/**
 * <p> 팀원 리스트 Dto
 *
 * <p>packageName    : com.beyond.backend.data.dto.teamDto
 * <p>fileName       : TeamMemberListDto
 * <p>author         : hongjm
 * <p>date           : 2025-02-22
 * <p>description    : teamNo로 팀원 리스트를 반환
 */
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-22        hongjm           최초 생성
 * 2025-02-25        hongjm           TeamJoinStatus 수정
 */

@Data
@Builder
public class TeamMemberListDto {

    private Long no;

    private String username;

    private Boolean isLeader;

    private TeamJoinStatus status;
}
