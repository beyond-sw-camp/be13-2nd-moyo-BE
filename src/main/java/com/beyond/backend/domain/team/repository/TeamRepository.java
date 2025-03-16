package com.beyond.backend.domain.team.repository;

import com.beyond.backend.domain.team.dto.TeamDetailDto;
import com.beyond.backend.domain.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
 * 2025-03-16        hongjm           팀 상세 정보 조회
 */

public interface TeamRepository extends JpaRepository<Team, Long> {
    /**
     * 팀 상세 정보 조회
     * @return TeamDetailDto

    @Query("SELECT new com.beyond.backend.domain.team.dto.TeamDetailDto" +
            "(t.teamName, t.teamIntroduce, t.projectStatus, ) " +
            "FROM Team t " +
            "JOIN Project p ON t.no = tu.team.no " +
            "WHERE (:userNo IS NULL OR tu.user.no = :userNo)")
    TeamDetailDto findByTeamDetail(@Param("teamNo") Long teamNo);
     */
}
