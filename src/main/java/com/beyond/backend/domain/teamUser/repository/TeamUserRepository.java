package com.beyond.backend.domain.teamUser.repository;

import com.beyond.backend.domain.team.dto.TeamDetailDto;
import com.beyond.backend.domain.team.dto.TeamMemberListDto;
import com.beyond.backend.domain.team.dto.TeamResponseDto;
import com.beyond.backend.domain.team.entity.TeamJoinStatus;
import java.util.List;

import com.beyond.backend.domain.team.dto.TeamSearchDto;
import com.beyond.backend.domain.teamUser.entity.TeamUser;
import com.beyond.backend.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * <p> 팀 유저 리포지토리
 *
 * <p>packageName    : com.beyond.backend.data.repository
 * <p>fileName       : TeamUserRepository
 * <p>author         : hongjm
 * <p>date           : 2025-02-20
 * <p>description    : 팀 유저 중간 테이블 리포지토리
 */
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-20        hongjm           최초 생성
 * 2025-02-22        hongjm           팀원 추가/제거 등등 기능 추가
 * 2025-02-23        hongjm           기능 추가 및 코드 정리
 * 2025-02-25        hongjm           TeamJoinStatus 수정
 * 2025-02-26        hongjm           중복 코드 통합
 */

public interface TeamUserRepository extends JpaRepository<TeamUser, Long> {

    /**
     * 모든 팀 정보 조회
     * @param userNo (필터) 유저번호
     * @param pageable 페이징 크기
     * @return TeamSearchDto
     */
    @Query("SELECT new com.beyond.backend.domain.team.dto.TeamResponseDto" +
            "(t.no, t.teamName, t.teamIntroduce, t.projectStatus) " +
            "FROM Team t " +
            "JOIN TeamUser tu ON t.no = tu.team.no " +
            "WHERE (:userNo IS NULL OR tu.user.no = :userNo)")
    Page<TeamResponseDto> findByUserNoForUserTeams(@Param("userNo") Long userNo, Pageable pageable);

    /**
     * 해당 팀의 리더 여부 확인
     * @param teamNo 팀번호
     * @param userNo 유저번호
     * @return Boolean
     */
    @Query("SELECT tu.isLeader " +
            "FROM TeamUser tu " +
            "JOIN User u ON tu.user.no = u.no " +
            "WHERE tu.team.no = :teamNo AND u.no = :userNo")
    Boolean isLeader(@Param("teamNo") Long teamNo, @Param("userNo") Long userNo);

    /**
     * 해당 팀에 속한 유저의 상태 조회
     * @param teamNo 팀번호
     * @param userNo 유저번호
     * @return 상태값 반환
     */
    @Query("SELECT tu.status " +
            "FROM TeamUser tu " +
            "JOIN User u ON tu.user.no = u.no " +
            "WHERE tu.team.no = :teamNo AND u.no = :userNo")
    TeamJoinStatus findByTeamStatus(@Param("teamNo") Long teamNo, @Param("userNo") Long userNo);

    /**
     * 해당 팀의 팀원 조회
     * @param teamNo 팀번호
     * @return TeamMemberListDto
     */
    @Query("SELECT new com.beyond.backend.domain.team.dto.TeamMemberListDto" +
            "(u.no, u.username, tu.isLeader, tu.status) " +
            "FROM Team t " +
            "JOIN TeamUser tu ON t.no = tu.team.no " +
            "JOIN User u ON u.no = tu.user.no " +
            "WHERE t.no = :teamNo AND tu.status = :status ")
    List<TeamMemberListDto> findByTeamNoForMember(@Param("teamNo") Long teamNo, @Param("status") TeamJoinStatus status);

    /**
     * 유저가 팀에 속해있는지 여부
     * @param teamNo 팀번호
     * @param userNo 유저번호
     * @return Boolean
     */
    @Query("SELECT COUNT(tu) > 0 " +
            "FROM TeamUser tu " +
            "WHERE tu.team.no = :teamNo AND tu.user.no = :userNo ")
    Boolean findByUserNoEquals (@Param("teamNo") Long teamNo, @Param("userNo") Long userNo);

    /**
     * 유저번호로 teamUserNo 검색
     * @param teamNo 팀번호
     * @param userNo 유저번호
     * @return Long TeamUser.no
     */
    @Query("SELECT tu.no " +
            "FROM TeamUser tu " +
            "WHERE tu.team.no = :teamNo AND tu.user.no = :userNo ")
    Long findByUserNoForTeamUserNo (@Param("teamNo") Long teamNo, @Param("userNo") Long userNo);


    @Query("SELECT new com.beyond.backend.domain.team.dto.TeamSearchDto(t.teamName, t.teamIntroduce, t.projectStatus) " +
            "FROM User u " +
            "JOIN TeamUser tu ON u.no = tu.user.no " +
            "JOIN Team t ON tu.team.no = t.no " +
            "WHERE u.no = :userNo")
    Page<TeamSearchDto> findUserTeams(@Param("userNo") Long userNo, Pageable pageable);

    // [홍도현] userNo가 teamNo에 속해 있는지 확인 (존재하면 true, 없으면 false 반환)
    boolean existsByUserNoAndTeamNo(Long userNo, Long teamNo);

    List<TeamUser> getTeamUserByUser(User user);
}
