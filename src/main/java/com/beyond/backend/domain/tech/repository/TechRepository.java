package com.beyond.backend.domain.tech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beyond.backend.domain.tech.entity.Tech;

import java.util.List;

public interface TechRepository extends JpaRepository<Tech, Long> {
	Boolean existsByTechName(String techName);


	// 프론트에서 string 으로 받아온 tech들 tech로 변환
	List<Tech> findByTechNameIn(List<String> techNames);
}

