package com.beyond.backend.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.beyond.backend.data.dto.ProjectDto;
import com.beyond.backend.data.dto.ProjectResponseDto;
import com.beyond.backend.data.entity.Project;
import com.beyond.backend.data.entity.Team;
import com.beyond.backend.data.entity.TeamUser;
import com.beyond.backend.data.entity.User;
import com.beyond.backend.data.repository.ProjectRepository;
import com.beyond.backend.data.repository.TeamRepository;
import com.beyond.backend.data.repository.TeamUserRepository;
import com.beyond.backend.data.repository.UserRepository;
import com.beyond.backend.service.ProjectService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

	private final UserRepository userRepository;
	private final TeamRepository teamRepository;
	private final ProjectRepository projectRepository;
	private final TeamUserRepository teamUserRepository;

	@Override
	public ProjectResponseDto createProject(ProjectDto projectDto) {

		// 팀이 존재하는지 검증
		Team team = teamRepository.findById(projectDto.getTeam().getNo()).orElseThrow(
			() -> new IllegalArgumentException("해당하는 팀이 없습니다.")
		);

		// 1. DTO -> entity 로 변환
		Project project = Project.builder()
			.name(projectDto.getName())
			.content(projectDto.getContent())
			.projectPurpose(projectDto.getProjectPurpose())
			.projectSubject(projectDto.getProjectSubject())
			.team(team) // 여기에 검증된 팀 넣기
			.build();

		// 2. repository 에 entity 저장
		projectRepository.save(project);

		// 3. entity -> responseDto 로 변환 후 반환
		return new ProjectResponseDto(project);
	}

	@Override
	public ProjectResponseDto updateProject(Long projectNo, Long userNo, ProjectDto projectDto) {

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

		// 4. 검증 후 수정
		project.update(projectDto);

		return new ProjectResponseDto(project);
	}

	@Override
	public ProjectResponseDto getProject(Long projectId) {

		Project project = projectRepository.findById(projectId).orElseThrow(
			() -> new IllegalArgumentException("해당하는 프로젝트가 없습니다.")
		);
		return new ProjectResponseDto(project);
	}

	@Override
	public void deleteProject(Long id) {
		projectRepository.deleteById(id);
	}

	@Override
	public Page<ProjectResponseDto> getProjectsByTeamNo(Long teamNo, Pageable pageable) {


		return null;
	}

	// @Override
	// public List<ProjectResponseDto> getProjectsByUserNo(Long userNo) {
	//
	// 	User user = userRepository.findById(userNo).orElseThrow(
	// 		() -> new IllegalArgumentException("해당하는 유저가 없습니다.")
	// 	);
	//
	// 	// projectrepo 에서 findByUserNo
	// 	List<Project> projectList = projectRepository.findByUserNo(userNo);
	//
	// 	return projectList.stream()
	// 					.map(ProjectResponseDto::new)
	// 					.toList();
	// }
}
