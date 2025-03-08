package com.beyond.backend.domain.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beyond.backend.domain.project.entity.Project;
import com.beyond.backend.domain.project.entity.ProjectTech;

public interface ProjectTechRepository extends JpaRepository<ProjectTech, Long> {

	void deleteAllByProject(Project project);
}
