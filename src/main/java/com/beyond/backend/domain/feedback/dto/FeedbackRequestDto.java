package com.beyond.backend.domain.feedback.dto;

import com.beyond.backend.domain.feedback.entity.FeedbackType;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackRequestDto {

	@NotBlank(message="피드백 내용은 필수 입력 항목입니다.")
	private String content;

}
