package com.beyond.backend.domain.project.service;

import com.beyond.backend.domain.project.dto.ProjectRequestDto;
import com.beyond.backend.domain.project.dto.ProjectResponseDto;
import com.beyond.backend.domain.project.entity.Project;
import com.beyond.backend.domain.project.entity.ProjectSearchOption;
import com.beyond.backend.domain.project.entity.ProjectTech;
import com.beyond.backend.domain.project.repository.ProjectRepository;
import com.beyond.backend.domain.project.repository.ProjectTechRepository;
import com.beyond.backend.domain.team.entity.Team;
import com.beyond.backend.domain.team.repository.TeamRepository;
import com.beyond.backend.domain.teamUser.repository.TeamUserRepository;
import com.beyond.backend.domain.tech.entity.Tech;
import com.beyond.backend.domain.tech.repository.TechRepository;
import com.beyond.backend.domain.user.entity.User;
import com.beyond.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

	private final UserRepository userRepository;
	private final TeamRepository teamRepository;
	private final ProjectRepository projectRepository;
	private final TeamUserRepository teamUserRepository;
	private final TechRepository techRepository;
	private final ProjectTechRepository projectTechRepository;

	@Override
	@Transactional
	public ProjectResponseDto createProject(ProjectRequestDto projectRequestDto) {

		// 팀이 존재하는지 검증
		Team team = teamRepository.findById(projectRequestDto.getTeamNo()).orElseThrow(
			() -> new IllegalArgumentException("해당하는 Team 이 없습니다.")
		);

		// 1. DTO -> entity 로 변환
		Project project = Project.builder()
			.name(projectRequestDto.getName())
			.content(projectRequestDto.getContent())
			.team(team) // 여기에 검증된 팀 넣기
			.build();
		projectRepository.save(project);

		// 3. techsNos 리스트를 순회하며 각 기술에 대해 ProjectTech 엔티티 생성
		List<ProjectTech> projectTechList = projectRequestDto.getTechsNos().stream()
				.map(techNo -> {
					// 각 techNo로 Tech 엔티티 조회
					Tech tech = techRepository.findById(techNo)
							.orElseThrow(() -> new IllegalArgumentException("해당 기술이 존재하지 않습니다. techNo=" + techNo));
					// ProjectTech 엔티티 생성: project.getNo()를 사용하여 project_no를 할당
					return ProjectTech.builder()
							.tech(tech)
							.project(project)
							.build();
				})
				.collect(Collectors.toList());

		// 4. 생성한 ProjectTech 엔티티들을 한 번에 저장
		projectTechRepository.saveAll(projectTechList);

		// 5. ProjectResponseDto 생성 후 반환
		return new ProjectResponseDto(project);

	}

	@Override
	public ProjectResponseDto updateProject(Long projectNo, Long userNo, ProjectRequestDto projectRequestDto) {
		// 1. 프로젝트 있는지 검증
		// 수정하고자 하는 프로젝트
		Project project = projectRepository.findById(projectNo).orElseThrow(
				() -> new IllegalArgumentException("해당하는 프로젝트가 없습니다.")
		);

		// 2. 유저 존재하는지 검증
		User user = userRepository.findById(userNo).orElseThrow(
				() -> new IllegalArgumentException("해당하는 사용자가 없습니다.")
		);

		Team team = project.getTeam();

		// 3. user 가 team 에 속하는가
		boolean existsByUserNoAndTeamNo = teamUserRepository.existsByUserNoAndTeamNo(user.getNo(), team.getNo());

		if (!existsByUserNoAndTeamNo) {
			throw new IllegalArgumentException("사용자는 해당 프로젝트를 수정할 권한이 없습니다.");
		}

		// 프로젝트 ID를 사용하여 새롭게 ProjectTech 저장
		List<ProjectTech> newProjectTechList = projectRequestDto.getTechsNos().stream()
				.map(techNo -> {
					Tech tech = techRepository.findById(techNo)
							.orElseThrow(() -> new IllegalArgumentException("해당 기술이 존재하지 않습니다. techNo=" + techNo));
					return ProjectTech.builder()
							.tech(tech)
							.project(project)
							.build();
				})
				.collect(Collectors.toList());

		// 5. 새롭게 저장

		project.update(projectRequestDto);
		projectTechRepository.saveAll(newProjectTechList);

		// 6. 업데이트된 프로젝트 정보 반환
		return new ProjectResponseDto(project);
	}

	//  모든 프로젝트 조회
	@Override
	public Page<ProjectResponseDto> getAllProjects(Pageable pageable) {
		return projectRepository.getProjects(pageable);
	}


	// 프로젝트 단건 조회
	@Override
	@Transactional
	public ProjectResponseDto getProjectByProjectNo(Long projectNo) {

		// 1. 프로젝트가 존재하는지 조회
		Project project = projectRepository.findById(projectNo).orElseThrow(
			() -> new IllegalArgumentException("해당 프로젝트가 존재하지 않습니다.")
		);

		// 조회수 증가
		project.increaseViewCnt();

		return new ProjectResponseDto(project);
	}


	// 유저가 참여한 모든 프로젝트 표시
	@Override
	public Page<ProjectResponseDto> getProjectsByUserNo(Long userNo, Pageable pageable) {

		// 1. 해당하는 유저
		User user = userRepository.findById(userNo).orElseThrow(
			() -> new IllegalArgumentException("해당하는 유저가 없습니다.")
		);

		// 2. user의 project list 가져오기
		Page<ProjectResponseDto> projectList = projectRepository.findProjectsByUserId(userNo, pageable);
		return projectList;
	}


	// 검색 결과 페이징 조회
	@Override
	public Page<ProjectResponseDto> searchProject(String keyword, ProjectSearchOption searchOption, Pageable pageable) {

		// 검증할 게 없음. 바로 검색결과 리턴해주기
		Page<ProjectResponseDto> projectSearchList = projectRepository.searchProject(keyword, searchOption, pageable);

		return projectSearchList;
	}

	@Override
	@Transactional
	public void deleteProject(Long userNo, Long projectNo) {

		// 1. user 검증
		User user = userRepository.findById(userNo).orElseThrow(
				()-> new IllegalArgumentException("해당하는 유저가 없습니다.")
		);


		// 2. project 검증
		Project project = projectRepository.findById(projectNo).orElseThrow(
				() -> new IllegalArgumentException("해당 프로젝트가 존재하지 않습니다.")
		);

		Team team = project.getTeam();

		// 3. user 가 team 에 속하는가
		boolean existsByUserNoAndTeamNo = teamUserRepository.existsByUserNoAndTeamNo(user.getNo(), project.getTeam().getNo());

		if (!existsByUserNoAndTeamNo) {
			throw new IllegalArgumentException("사용자는 해당 프로젝트를 수정할 권한이 없습니다.");
		}

		//  4. 리더가 아니면 예외
		//// 프로젝트 방장 인지 검증 로직 추가 <- 방장만 프로젝트 삭제 가능
		if( !teamUserRepository.isLeader(team.getNo(), userNo) ){
			throw new IllegalArgumentException("사용자는 해당 프로젝트를 수정할 권한이 없습니다.");
		}

		// 5. 프로젝트 삭제
		projectRepository.deleteById(projectNo);
	}
}