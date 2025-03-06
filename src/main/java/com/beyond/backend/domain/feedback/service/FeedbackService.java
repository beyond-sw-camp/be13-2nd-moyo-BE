package com.beyond.backend.domain.feedback.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.beyond.backend.domain.feedback.dto.FeedbackRequestDto;
import com.beyond.backend.domain.feedback.dto.FeedbackResponseDto;
import com.beyond.backend.domain.feedback.dto.FeedbackUpdateRequestDto;

public interface FeedbackService {

	FeedbackResponseDto createFeedback(Long userNo, Long projectNo, FeedbackRequestDto dto);

	FeedbackResponseDto updateFeedback(Long userNo, Long projectNo, Long feedbackNo, FeedbackUpdateRequestDto dto);

	void deleteFeedback(Long userNo, Long feedbackNo);

	// 1. 내가 쓴 피드백 조회
	Page<FeedbackResponseDto> getFeedbackByUserNo(Long userNo, Pageable pageable);

	// 2. 팀 프로젝트 에서 피드백 조회
	Page<FeedbackResponseDto> getFeedbackByProjectNo(Long userNo,Long projectNo, Pageable pageable);

}
