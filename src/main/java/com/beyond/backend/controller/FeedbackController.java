package com.beyond.backend.controller;

import com.beyond.backend.domain.feedback.dto.FeedbackRequestDto;
import com.beyond.backend.domain.feedback.dto.FeedbackResponseDto;
import com.beyond.backend.domain.feedback.dto.FeedbackUpdateRequestDto;
import com.beyond.backend.domain.feedback.entity.FeedbackType;
import com.beyond.backend.domain.feedback.service.FeedbackService;
import com.beyond.backend.domain.user.dto.CustomUserDetails;
import com.beyond.backend.domain.user.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "08 피드백 API", description = "피드백 API")
@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
@Log4j2
public class FeedbackController {

	private final FeedbackService feedbackService;
	private final AuthService authService;

	@Operation(summary = "피드백 생성 메서드", description = "피드백 생성 메서드입니다.")
	@PostMapping("/project/{projectNo}/createFeedback")
		public ResponseEntity<FeedbackResponseDto> createFeedback(@PathVariable Long projectNo,
																  @RequestParam FeedbackType feedbackType,
																  @AuthenticationPrincipal CustomUserDetails userDetails,
																  @Valid @RequestBody FeedbackRequestDto dto) {

		FeedbackResponseDto feedbackResponseDto = feedbackService.createFeedback(projectNo,feedbackType, dto, userDetails.getUser().getNo());
		return ResponseEntity.ok(feedbackResponseDto);
	}
	// 팀원들만 수정 가능
	// feedbackNo로
	@Operation(summary = "피드백 수정 메서드", description = "피드백 수정 메서드입니다.")
	@PreAuthorize("hasPermission(#feedbackNo, 'FEEDBACK')")
	@PostMapping("/project/{projectNo}/update/{feedbackNo}")
	public ResponseEntity<FeedbackResponseDto> updateFeedback(@PathVariable Long projectNo,
															  @PathVariable Long feedbackNo,
															  @RequestParam FeedbackType feedbackType,
															  @Valid @RequestBody FeedbackUpdateRequestDto dto) {

		FeedbackResponseDto feedbackResponseDto = feedbackService.updateFeedback(projectNo, feedbackNo, feedbackType, dto);

		return ResponseEntity.ok(feedbackResponseDto);
	}

	@Operation(summary = "피드백 삭제 메서드", description = "피드백 삭제 메서드입니다.")
	@DeleteMapping("/{feedbackNo}")
	@PreAuthorize("hasRole('ADMIN') or hasPermission(#feedbackNo, 'FEEDBACK_DELETE')")
	public ResponseEntity<String> deleteFeedback(
			@PathVariable Long feedbackNo) {

		feedbackService.deleteFeedback(feedbackNo);
		return ResponseEntity.status(HttpStatus.OK).body("정상적으로 삭제되었습니다.");
	}

	@Operation(summary = "사용자가 작성한 모든 피드백 조회 메서드", description = "피드백 조회 메서드입니다.")
	@GetMapping("/list")
	public ResponseEntity<Page<FeedbackResponseDto>> getFeedbackByUserNo(@AuthenticationPrincipal CustomUserDetails userDetails,
																		 @PageableDefault(size = 10, page = 0) Pageable pageable){

		Page<FeedbackResponseDto> feedbackByUserNo = feedbackService.getFeedbackByUserNo(userDetails.getUser().getNo(), pageable);
		return ResponseEntity.ok(feedbackByUserNo);
	}

	@Operation(summary = "사용자가 참여한 프로젝트에서의 피드백 조회 메서드", description = "사용자가 참여한 프로젝트에서의 피드백 조회")
	@GetMapping("/list/{projectNo}")
	public ResponseEntity<Page<FeedbackResponseDto>> getFeedbackByProjectNo(
													@AuthenticationPrincipal CustomUserDetails userDetails,
													@PathVariable Long projectNo,
													@PageableDefault(size = 10, page = 0) Pageable pageable){

		Page<FeedbackResponseDto> feedbackByUserNo = feedbackService.getFeedbackByProjectNo(userDetails.getUser().getNo(), projectNo, pageable);
		return ResponseEntity.ok(feedbackByUserNo);
	}
}
