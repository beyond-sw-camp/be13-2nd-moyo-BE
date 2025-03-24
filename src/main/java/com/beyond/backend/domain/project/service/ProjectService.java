package com.beyond.backend.domain.project.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.beyond.backend.domain.project.dto.ProjectRequestDto;
import com.beyond.backend.domain.project.dto.ProjectResponseDto;
import com.beyond.backend.domain.project.dto.ProjectUpdateRequestDto;
import com.beyond.backend.domain.project.entity.ProjectSearchOption;
import com.beyond.backend.domain.project.entity.ProjectSortOption;
import com.beyond.backend.domain.project.entity.ProjectStatus;

/**
 * <p>
 *
 * <p>packageName    : com.beyond.backend.service
 * <p>fileName       : ProjectService
 * <p>author         : jaewoo
 * <p>date           : 2025. 2. 2.
 * <p>description    : 프로젝트 Service
 */

public interface ProjectService {

    // 프로젝트 생성
    ProjectResponseDto createProject(ProjectRequestDto projectRequestDto, Long userNo);

    // 프로젝트 수정
    ProjectResponseDto updateProject(Long projectNo, ProjectStatus projectStatus, ProjectUpdateRequestDto projectRequestDto);

    // 프로젝트 삭제
    void deleteProject(Long projectNo);


    // 프로젝트 전체 보기
    Page<ProjectResponseDto> getAllProjects(Pageable pageable, ProjectSortOption projectSortOption);

    // 프로젝트 단건 조회
    ProjectResponseDto getProjectByProjectNo(Long projectNo);


    // 유저가 참여한 모든 프로젝트 조회
    Page<ProjectResponseDto> getProjectsByUserNo(Long userNo, Pageable pageable);

    // 유저가 참여한 프로젝트 중, 검색 조회
    Page<ProjectResponseDto> searchProject(String keyword, ProjectSearchOption searchOption, Pageable pageable);

    void viewProject(Long projectNo, HttpServletRequest request);

}
