package com.beyond.backend.domain.tech.service;

import java.util.List;
import java.util.stream.Collectors;
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

	/// TO DO = 관리자 검증 로직 다 넣어야 한다!!!!!!!!!!

	@Override
	public TechResponseDto createTech(TechRequestDto dto) {

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
	public void deleteTech(Long no) {
		techRepository.findById(no)
			.orElseThrow(() -> new IllegalArgumentException("해당하는 기술이 없습니다"));
		techRepository.deleteById(no);
	}
}
