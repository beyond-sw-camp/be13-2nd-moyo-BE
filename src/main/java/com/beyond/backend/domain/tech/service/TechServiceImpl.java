package com.beyond.backend.domain.tech.service;

import java.util.List;
import java.util.stream.Collectors;

import com.beyond.backend.domain.user.dto.CustomUserDetails;
import com.beyond.backend.domain.user.jwt.JwtTokenProvider;
import com.beyond.backend.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beyond.backend.domain.tech.dto.TechRequestDto;
import com.beyond.backend.domain.tech.dto.TechResponseDto;
import com.beyond.backend.domain.tech.entity.Tech;
import com.beyond.backend.domain.tech.repository.TechRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TechServiceImpl implements TechService {

	private final TechRepository techRepository;
	private final JwtTokenProvider jwtTokenProvider;


	@Override
	public TechResponseDto createTech(HttpServletRequest request, TechRequestDto dto) {
		validationAdmin(request);

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
		validationAdmin(request);

		techRepository.findById(no)
			.orElseThrow(() -> new IllegalArgumentException("해당하는 기술이 없습니다"));
		techRepository.deleteById(no);
	}

	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7); // "Bearer " 부분 제거
		}
		return null;
	}

	private void validationAdmin(HttpServletRequest request) {
		String token = resolveToken(request);

		if (token == null || !jwtTokenProvider.validateToken(token)) {
			throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
		}

		Authentication authentication = jwtTokenProvider.getAuthentication(token);
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

		// 관리자 권한 체크
		boolean isAdmin = userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(role -> role.equals("ROLE_ADMIN"));

		if (!isAdmin) {
			throw new SecurityException("관리자 권한이 필요합니다.");
		}
	}
}