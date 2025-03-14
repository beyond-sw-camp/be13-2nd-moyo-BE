package com.beyond.backend.domain.tech.service;

import com.beyond.backend.domain.common.exception.ProjectException;
import com.beyond.backend.domain.common.exception.message.ExceptionMessage;
import com.beyond.backend.domain.tech.dto.TechRequestDto;
import com.beyond.backend.domain.tech.dto.TechResponseDto;
import com.beyond.backend.domain.tech.entity.Tech;
import com.beyond.backend.domain.tech.repository.TechRepository;
import com.beyond.backend.domain.user.dto.CustomUserDetails;
import com.beyond.backend.domain.user.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TechServiceImpl implements TechService {

	private final TechRepository techRepository;
	private final JwtTokenProvider jwtTokenProvider;

	/// 관리자용

	@Override
	public TechResponseDto createTech(HttpServletRequest request, TechRequestDto dto) {

		if (techRepository.existsByTechName(dto.getTechName())) {
			throw new IllegalArgumentException("이미 존재하는 기술입니다");
		}

		Tech tech = new Tech(dto.getTechName());
		techRepository.save(tech);
		return new TechResponseDto(tech);
	}

	@Override
	@Transactional(readOnly = true)
	public List<TechResponseDto> findAllTech() {
		return techRepository.findAll()
			.stream()
			.map(TechResponseDto::new)
			.collect(Collectors.toList());
	}

	@Override
	public void deleteTech(HttpServletRequest request, Long no) {

		techRepository.findById(no)
						.orElseThrow(() -> new ProjectException(ExceptionMessage.TECH_NOT_FOUND));

		techRepository.deleteById(no);
	}

}

