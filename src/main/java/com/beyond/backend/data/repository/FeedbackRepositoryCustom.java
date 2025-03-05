package com.beyond.backend.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.beyond.backend.data.dto.FeedbackResponseDto;

public interface FeedbackRepositoryCustom {



	// 1. 내가 쓴 피드백 조회
	Page<FeedbackResponseDto> findFeedbackByUserId(Long userNo, Pageable pageable);

	// 2. 팀 프로젝트 에서 피드백 조회
	Page<FeedbackResponseDto> findFeedbackByProjectId(Long projectNo, Pageable pageable);

}
