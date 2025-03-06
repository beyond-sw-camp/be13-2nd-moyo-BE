package com.beyond.backend.domain.project.repository;

import com.beyond.backend.domain.project.entity.ProjectTech;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectTechRepository extends JpaRepository<ProjectTech, Long> {
}
