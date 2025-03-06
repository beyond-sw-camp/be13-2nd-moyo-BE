package com.beyond.backend.domain.feedback.dto;

import com.beyond.backend.domain.feedback.entity.FeedbackType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackRequestDto {

	private String content;
	private FeedbackType feedbackType;

}
