package com.beyond.backend.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beyond.backend.data.entity.Tech;

public interface TechRepository extends JpaRepository<Tech, Long> {
	Boolean existsByTechName(String techName);
}

