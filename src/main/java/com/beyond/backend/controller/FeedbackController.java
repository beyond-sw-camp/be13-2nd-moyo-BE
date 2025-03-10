package com.beyond.backend.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.beyond.backend.domain.feedback.dto.FeedbackRequestDto;
import com.beyond.backend.domain.feedback.dto.FeedbackResponseDto;
import com.beyond.backend.domain.feedback.dto.FeedbackUpdateRequestDto;
import com.beyond.backend.domain.feedback.entity.FeedbackType;
import com.beyond.backend.domain.feedback.service.FeedbackService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedbackController {

	private final FeedbackService feedbackService;
	@Operation(summary = "피드백 생성 메서드", description = "피드백 생성 메서드입니다.")
	@PostMapping("/create/{userNo}/{projectNo}")
		public ResponseEntity<FeedbackResponseDto> createFeedback(@PathVariable Long userNo,
																  @PathVariable Long projectNo,
																  @RequestParam FeedbackType feedbackType,
																  @RequestBody FeedbackRequestDto dto) {

		FeedbackResponseDto feedbackResponseDto = feedbackService.createFeedback(userNo, projectNo,feedbackType, dto);
		return ResponseEntity.ok(feedbackResponseDto);
	}
	@Operation(summary = "피드백 수정 메서드", description = "피드백 수정 메서드입니다.")
	@PostMapping("/update/{userNo}/{projectNo}/{feedbackNo}")
	public ResponseEntity<FeedbackResponseDto> updateFeedback(@PathVariable Long userNo,
															  @PathVariable Long projectNo,
															  @PathVariable Long feedbackNo,
															  @RequestParam FeedbackType feedbackType,
															  @RequestBody FeedbackUpdateRequestDto dto) {

		FeedbackResponseDto feedbackResponseDto = feedbackService.updateFeedback(userNo, projectNo, feedbackNo, feedbackType, dto);

		return ResponseEntity.ok(feedbackResponseDto);
	}

	@Operation(summary = "피드백 삭제 메서드", description = "피드백 삭제 메서드입니다.")
	@DeleteMapping("/delete/{userNo}/{feedbackNo}")
	public ResponseEntity<String> deleteFeedback(@PathVariable Long userNo,
											     @PathVariable Long feedbackNo) {

		feedbackService.deleteFeedback(userNo, feedbackNo);
		return ResponseEntity.status(HttpStatus.OK).body("정상적으로 삭제되었습니다.");
	}

	@Operation(summary = "사용자가 작성한 모든 피드백 조회 메서드", description = "피드백 조회 메서드입니다.")
	@GetMapping("/{userNo}")
	public ResponseEntity<Page<FeedbackResponseDto>> getFeedbackByUserNo(
													@PathVariable Long userNo,
													@PageableDefault(size = 10, page = 0) Pageable pageable){

		Page<FeedbackResponseDto> feedbackByUserNo = feedbackService.getFeedbackByUserNo(userNo, pageable);
		return ResponseEntity.ok(feedbackByUserNo);
	}

	@Operation(summary = "사용자가 참여한 프로젝트에서의 피드백 조회 메서드", description = "사용자가 참여한 프로젝트에서의 피드백 조회")
	@GetMapping("/list/{userNo}/{projectNo}")
	public ResponseEntity<Page<FeedbackResponseDto>> getFeedbackByProjectNo(
													@PathVariable Long userNo,
													@PathVariable Long projectNo,
													@PageableDefault(size = 10, page = 0) Pageable pageable){

		Page<FeedbackResponseDto> feedbackByUserNo = feedbackService.getFeedbackByProjectNo(userNo, projectNo, pageable);
		return ResponseEntity.ok(feedbackByUserNo);
	}
}
