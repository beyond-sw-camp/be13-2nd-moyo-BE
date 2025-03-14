package com.beyond.backend.domain.project.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProjectUpdateRequestDto {

	@NotBlank(message= "프로젝트 제목은 필수 입력 항목입니다.")
	private String name;

	@NotBlank(message= "프로젝트 내용은 필수 입력 항목입니다.")
	private String content;

	private Long teamNo;

	private List<Long> techsNos;
}
