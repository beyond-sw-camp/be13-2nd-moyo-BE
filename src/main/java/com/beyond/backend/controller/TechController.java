package com.beyond.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beyond.backend.domain.tech.dto.TechRequestDto;
import com.beyond.backend.domain.tech.dto.TechResponseDto;
import com.beyond.backend.domain.tech.service.TechService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/techs")  // 공통 경로 설정
@RequiredArgsConstructor
public class TechController {

	private final TechService techService;

	/**
	 * 기술 생성 API
	 * @param dto 생성할 기술 정보
	 * @return 생성된 기술 정보
	 */
	@PostMapping
	public ResponseEntity<TechResponseDto> createTech(@RequestBody TechRequestDto dto) {

		TechResponseDto response = techService.createTech(dto);

		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<List<TechResponseDto>> getAllTechs() {
		List<TechResponseDto> techList = techService.findAllTech();
		return ResponseEntity.ok(techList);
	}

	/**
	 * 기술 삭제 API
	 * @param id 삭제할 기술의 ID
	 * @return 성공 메시지
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteTech(@PathVariable Long id) {
		techService.deleteTech(id);
		return ResponseEntity.ok("기술이 성공적으로 삭제되었습니다.");
	}
}
