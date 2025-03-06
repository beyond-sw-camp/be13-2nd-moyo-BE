package com.beyond.backend.domain.feedback.dto;

import com.beyond.backend.domain.feedback.entity.Feedback;
import com.beyond.backend.domain.feedback.entity.FeedbackType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponseDto {

	private Long feedbackNo;
	private String content;
	private FeedbackType feedbackType;

	private String userName;
	private String projectName;


	public FeedbackResponseDto(Feedback feedback) {
		this.feedbackNo = feedback.getNo();
		this.content = feedback.getContent();
		this.feedbackType = feedback.getFeedbackType();
		this.userName = feedback.getUser().getUsername();
		this.projectName = feedback.getProject().getName();
	}

}
