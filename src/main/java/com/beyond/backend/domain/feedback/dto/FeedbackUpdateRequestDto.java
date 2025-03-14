package com.beyond.backend.domain.feedback.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FeedbackUpdateRequestDto {

	@NotBlank(message="피드백 내용은 필수 입력 항목입니다.")
	String content;
}
