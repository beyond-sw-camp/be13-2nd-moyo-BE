package com.beyond.backend.domain.tech.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beyond.backend.domain.tech.entity.Tech;

public interface TechRepository extends JpaRepository<Tech, Long> {
	Boolean existsByTechName(String techName);
}

