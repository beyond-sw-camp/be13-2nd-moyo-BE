package com.beyond.backend.service;

import java.util.List;

import com.beyond.backend.data.dto.tech.TechRequestDto;
import com.beyond.backend.data.dto.tech.TechResponseDto;

public interface TechService {

	TechResponseDto createTech(TechRequestDto dto);

	List<TechResponseDto> findAllTech();

	void deleteTech(Long no);
}
