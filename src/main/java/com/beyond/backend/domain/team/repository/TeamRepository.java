package com.beyond.backend.domain.team.repository;

import com.beyond.backend.domain.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <p> 팀 리포지토리
 * 
 * <p>packageName    : com.beyond.backend.data.repository
 * <p>fileName       : TeamRepository
 * <p>author         : hongjm
 * <p>date           : 2025-02-03
 * <p>description    : 팀 관련 리포지토리
 */
 /*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-03        hongjm           최초 생성
 */

public interface TeamRepository extends JpaRepository<Team, Long> {


}
