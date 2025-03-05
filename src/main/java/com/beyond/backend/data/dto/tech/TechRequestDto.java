package com.beyond.backend.data.dto.tech;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TechRequestDto {


	@NotBlank
	private String techName;
}
