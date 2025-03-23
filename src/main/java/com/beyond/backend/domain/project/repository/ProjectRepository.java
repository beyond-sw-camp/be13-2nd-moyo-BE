package com.beyond.backend.domain.project.repository;

import com.beyond.backend.domain.project.entity.Project;
import com.beyond.backend.domain.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectRepositoryCustom {

	@Modifying
	@Transactional
	@Query("UPDATE Project p SET p.viewCnt = p.viewCnt + 1 WHERE p.no =:projectNo")
	void increaseViewCount(Long projectNo);

	List<Project> getTeamByTeam_No(Long teamNo);

	Long team(Team team);
}
