package com.beyond.backend.domain.tech.service;

import java.util.List;

import com.beyond.backend.domain.tech.dto.TechRequestDto;
import com.beyond.backend.domain.tech.dto.TechResponseDto;

public interface TechService {

	TechResponseDto createTech(TechRequestDto dto);

	List<TechResponseDto> findAllTech();

	void deleteTech(Long no);
}
