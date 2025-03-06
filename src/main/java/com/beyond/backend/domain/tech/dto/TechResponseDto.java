package com.beyond.backend.domain.tech.dto;

import com.beyond.backend.domain.tech.entity.Tech;

import lombok.Data;

@Data
public class TechResponseDto {

	private String techName;

	public TechResponseDto(Tech tech) {
		this.techName = tech.getTechName();
	}
}
