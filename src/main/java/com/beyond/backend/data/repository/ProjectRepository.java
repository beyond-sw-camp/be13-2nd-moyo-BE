package com.beyond.backend.data.repository;

import java.util.List;

import com.beyond.backend.data.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025. 2. 2.        jaewoo             최초 생성
 * 2025. 2. 2.        jaewoo             오타 수정
 * 2025. 2. 17.       jaewoo             findByTeamNo 함수 생성
 * 2025. 2. 18.       jaewoo             Pageable 추가
 */

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<Project> findByTeamNo(Long teamNo, Pageable pageable);

}
