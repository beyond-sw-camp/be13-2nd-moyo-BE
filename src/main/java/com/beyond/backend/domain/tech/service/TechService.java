package com.beyond.backend.domain.tech.service;

import java.util.List;

import com.beyond.backend.domain.tech.dto.TechRequestDto;
import com.beyond.backend.domain.tech.dto.TechResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface TechService {

	TechResponseDto createTech(HttpServletRequest request, TechRequestDto dto);

	List<TechResponseDto> findAllTech();

	void deleteTech(HttpServletRequest request, Long no);
}
