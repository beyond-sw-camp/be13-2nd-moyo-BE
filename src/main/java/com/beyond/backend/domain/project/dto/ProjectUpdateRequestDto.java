package com.beyond.backend.domain.project.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProjectUpdateRequestDto {
	private String name;

	private String content;

	private Long teamNo;

	private List<Long> techsNos;
}
