package com.beyond.backend.service;

import com.beyond.backend.data.dto.teamDto.TeamDto;
import com.beyond.backend.data.dto.teamDto.TeamMemberListDto;
import com.beyond.backend.data.dto.teamDto.TeamResponseDto;
import com.beyond.backend.data.entity.ProjectStatus;
import com.beyond.backend.data.entity.TeamJoinStatus;
import org.springframework.data.domain.PageImpl;

import java.util.List;

/**
 * <p> 팀 서비스
 *
 * <p>packageName    : com.beyond.backend.service
 * <p>fileName       : TeamService
 * <p>author         : hongjm
 * <p>date           : 2025-02-03
 * <p>description    : 팀 서비스
 */
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-03        hongjm           최초 생성
 * 2025-02-15        hongjm           팀 검색 기능 추가
 * 2025-02-18        hongjm           팀 검색 페이징 기능 추가 및 보완
 * 2025-02-20        hongjm           팀 CRUD 수정
 * 2025-02-22        hongjm           팀원 추가/제거 등등 기능 추가
 * 2025-02-24        hongjm           코드 정리
 * 2025-02-26        hongjm           중복 코드 통합
 */

public interface TeamService {
    // 팀 생성
    TeamResponseDto createTeam(TeamDto teamDto);
    
    // 팀 정보 업데이트
    TeamResponseDto updateTeam(TeamDto team) throws Exception;

    // 모든 팀 정보 조회
    PageImpl<TeamResponseDto> filterUserTeams(
            Long userNo, String teamName, String teamIntroduce, ProjectStatus projectStatus, int page, int size);

    // 팀 삭제
    void deleteTeam(Long no) throws Exception;

    // 팀원 목록 조회
    List<TeamMemberListDto> getTeamMembers(Long teamNo) throws Exception;

    // [팀장] 팀원 신청 목록 조회
    List<TeamMemberListDto> getTeamMemberRequest(Long teamNo, Long userNo, TeamJoinStatus status) throws Exception;

    // [팀장] 팀원 신청 수락
    void teamAccept(Long teamNo, Long userNo) throws Exception;
    
    // [팀장] 팀원 신청 거부
    void teamDelete(Long teamNo, Long userNo) throws Exception;

    // 팀원 신청
    void teamJoinRequest(Long teamNo, Long userNo) throws Exception;
    
    // 팀원 신청 취소 / 탈퇴
    void teamJoinRequestCancel(Long teamNo, Long userNo) throws Exception;
}
