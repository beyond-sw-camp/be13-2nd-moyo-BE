package com.beyond.backend.data.dto.tech;

import com.beyond.backend.data.entity.Tech;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TechResponseDto {

	private String techName;

	public TechResponseDto(Tech tech) {
		this.techName = tech.getTechName();
	}
}
