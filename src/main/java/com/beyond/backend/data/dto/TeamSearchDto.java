package com.beyond.backend.data.dto;

import com.beyond.backend.data.entity.ProjectStatus;

import lombok.Builder;
import lombok.Data;

/**
 * <p> 팀 검색 Dto
 *
 * <p>packageName    : com.beyond.backend.data.dto
 * <p>fileName       : TeamUserDto
 * <p>author         : hongjm
 * <p>date           : 2025-02-16
 * <p>description    : userNo로 팀을 반환
 */
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-16        hongjm           최초 생성
 * 2025-02-18        hongjm           간소화
 */

@Data
@Builder
public class TeamSearchDto {

    private String teamName;

    private String teamIntroduce;

    private ProjectStatus projectStatus;

}
