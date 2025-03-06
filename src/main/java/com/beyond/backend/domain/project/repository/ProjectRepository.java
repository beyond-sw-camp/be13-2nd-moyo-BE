package com.beyond.backend.domain.project.repository;

import com.beyond.backend.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <p>
 *
 * <p>packageName    : com.beyond.backend.data.repository
 * <p>fileName       : ProjectRepository
 * <p>author         : jaewoo
 * <p>date           : 2025. 2. 2.
 * <p>description    : 프로젝트 Repository
 */


public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectRepositoryCustom {
}
