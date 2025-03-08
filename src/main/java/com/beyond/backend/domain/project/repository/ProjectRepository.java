package com.beyond.backend.domain.project.repository;

import com.beyond.backend.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectRepositoryCustom {
}
