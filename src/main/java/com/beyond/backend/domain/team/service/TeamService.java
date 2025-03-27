package com.beyond.backend.domain.team.service;

import com.beyond.backend.domain.team.dto.TeamDetailDto;
import com.beyond.backend.domain.team.dto.TeamDto;
import com.beyond.backend.domain.team.dto.TeamLeaderDto;
import com.beyond.backend.domain.team.dto.TeamMemberListDto;
import com.beyond.backend.domain.team.dto.TeamResponseDto;
import com.beyond.backend.domain.project.entity.ProjectStatus;
import com.beyond.backend.domain.team.entity.TeamJoinStatus;
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
 * 2025-03-21        hongjm           팀 리더 확인, 상세보기
 */

public interface TeamService {
    // 팀 생성
    TeamResponseDto createTeam(Long userNo, TeamDto teamDto);
    
    // 팀 정보 업데이트
    void updateTeam(Long userNo, TeamResponseDto team) throws Exception;

    // 모든 팀 정보 조회
    PageImpl<TeamResponseDto> filterUserTeams(
            Long userNo, String teamName, String teamIntroduce, ProjectStatus projectStatus, int page, int size);
    // 팀 삭제
    void deleteTeam(Long userNo, Long teamNo) throws Exception;
    
    // 팀 상세조회
    TeamDetailDto getTeamDetailDto(Long teamNo) throws Exception;

    TeamLeaderDto isTeamLeader(Long userNo, Long teamNo, Long projectNo);

    // 팀원 목록 조회
    List<TeamMemberListDto> getTeamMembers(Long teamNo) throws Exception;

    // [팀장] 팀원 신청 목록 조회
    List<TeamMemberListDto> getTeamMemberRequest(Long userNo, Long teamNo, TeamJoinStatus status) throws Exception;

    // [팀장] 팀원 신청 수락
    void teamAccept(Long teamNo, Long userNo) throws Exception;

    // [팀장] 팀원 신청 거부
    void teamDelete(Long teamNo, Long userNo) throws Exception;

    // 팀원 신청
    void teamJoinRequest(Long userNo, Long teamNo) throws Exception;

    // 팀원 신청 취소 / 탈퇴
    void teamJoinRequestCancel(Long userNo, Long teamNo) throws Exception;

    // [팀장] 리더 권한 넘겨주기

    void teamLeaderSwap(Long currentUserNo, Long teamNo, Long nextUserNo) throws Exception;

    String findLeaderUsernameByTeamNo(Long teamNo);

    TeamMemberListDto changeTeamRole(Long teamNo, Long userNo, Long no, String newRole);
}
