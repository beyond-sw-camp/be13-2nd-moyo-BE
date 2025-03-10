package com.beyond.backend.domain.project.dto;

import java.util.List;

import com.beyond.backend.domain.project.entity.ProjectStatus;

import lombok.Data;

@Data
public class ProjectUpdateRequestDto {
	private String name;

	private String content;

	private Long teamNo;

	private Long userNo;

	private List<Long> techsNos;
}
