package com.beyond.backend.service;

import com.beyond.backend.data.dto.ProjectDto;
import com.beyond.backend.data.dto.ProjectResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * <p>
 *
 * <p>packageName    : com.beyond.backend.service
 * <p>fileName       : ProjectService
 * <p>author         : jaewoo
 * <p>date           : 2025. 2. 2.
 * <p>description    : 프로젝트 Service
 */
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025. 2. 2.        jaewoo             최초 생성
 * 2025. 2. 3.        jaewoo             파라미터 명 변경
 * 2025. 2. 4.        jaewoo             파라미터 명 변경
 * 2025. 2. 16        jaewoo             getUserProjects 함수 추가
 * 2025. 2. 17        jaewoo             getProjectsByUsername 함수명 변경
 * 2025. 2. 17        jaewoo             getProjectsByTeamNo 함수명 변경
 * 2025. 2. 18        jaewoo             Pageable 추가
 */

public interface ProjectService {

    // 프로젝트 생성
    ProjectResponseDto createProject(ProjectDto projectDto);

    // 프로젝트 수정
    ProjectResponseDto updateProject(Long projectNo, Long teamNo, ProjectDto projectDto);

    // 프로젝트 id로 단건 조회
    ProjectResponseDto getProject(Long projectNo);

    // 프로젝트 삭제
    void deleteProject(Long id);

    // 팀 별 프로젝트 조회 !
    Page<ProjectResponseDto> getProjectsByTeamNo(Long teamNo, Pageable pageable);

    // 유저가 참여한 모든 프로젝트 조회
    // List<ProjectResponseDto> getProjectsByUserNo(Long userNo);

}
