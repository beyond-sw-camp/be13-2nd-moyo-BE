package com.beyond.backend.data.dto;

import com.beyond.backend.data.entity.FeedbackType;

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
