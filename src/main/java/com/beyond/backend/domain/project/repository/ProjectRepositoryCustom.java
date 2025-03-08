package com.beyond.backend.domain.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.beyond.backend.domain.project.dto.ProjectResponseDto;
import com.beyond.backend.domain.project.entity.ProjectSearchOption;

public interface ProjectRepositoryCustom {

	// 모든 프로젝트 조회
	Page<ProjectResponseDto> getProjects(Pageable pageable);


	// 프로젝트 검색 조회
	Page<ProjectResponseDto> searchProject(String keyword, ProjectSearchOption projectSearchOption, Pageable pageable);


	// user가 참여한 모든 프로젝트 조회
	Page<ProjectResponseDto> findProjectsByUserId(Long userNo, Pageable pageable);

}
