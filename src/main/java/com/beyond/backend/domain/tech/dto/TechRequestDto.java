package com.beyond.backend.domain.tech.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TechRequestDto {


	@NotBlank
	private String techName;
}
