package com.beyond.backend.domain.project.service;

import com.beyond.backend.domain.common.exception.*;
import com.beyond.backend.domain.common.exception.message.ExceptionMessage;
import com.beyond.backend.domain.post.repository.PostRepository;
import com.beyond.backend.domain.post.service.PostService;
import com.beyond.backend.domain.project.dto.ProjectRequestDto;
import com.beyond.backend.domain.project.dto.ProjectResponseDto;
import com.beyond.backend.domain.project.dto.ProjectUpdateRequestDto;
import com.beyond.backend.domain.project.entity.Project;
import com.beyond.backend.domain.project.entity.ProjectSearchOption;
import com.beyond.backend.domain.project.entity.ProjectSortOption;
import com.beyond.backend.domain.project.entity.ProjectStatus;
import com.beyond.backend.domain.project.entity.ProjectTech;
import com.beyond.backend.domain.project.repository.ProjectRepository;
import com.beyond.backend.domain.project.repository.ProjectTechRepository;
import com.beyond.backend.domain.team.entity.Team;
import com.beyond.backend.domain.team.repository.TeamRepository;
import com.beyond.backend.domain.teamUser.repository.TeamUserRepository;
import com.beyond.backend.domain.tech.entity.Tech;
import com.beyond.backend.domain.tech.repository.TechRepository;
import com.beyond.backend.domain.user.dto.CustomUserDetails;
import com.beyond.backend.domain.user.entity.User;
import com.beyond.backend.domain.user.repository.UserRepository;
import com.beyond.backend.domain.user.service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
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
	private final AuthService authService;
	private final RedisTemplate<String, String> redisTemplate;

	@Override
	@Transactional
	public ProjectResponseDto createProject(ProjectRequestDto projectRequestDto) {

		CustomUserDetails userDetails = authService.getCurrentUser();

		// 1. 팀이 존재하는지 검증
		Team team = teamRepository.findById(projectRequestDto.getTeamNo()).orElseThrow(
			() -> new TeamException(ExceptionMessage.TEAM_NOT_FOUND));


		// 2. 회원이 팀에 속하는지
		boolean existsByUserNoAndTeamNo = teamUserRepository.existsByUserNoAndTeamNo(userDetails.getNo(), team.getNo());

		if (!existsByUserNoAndTeamNo) {
			throw new IllegalArgumentException("사용자는 해당 팀에 속하지 않습니다.");
		}

		// 3. DTO -> entity 로 변환
		Project project = Project.builder()
								.name(projectRequestDto.getName())
								.content(projectRequestDto.getContent())
								.team(team) // 여기에 검증된 팀 넣기
								.build();

		projectRepository.save(project);

		// 3. techsNo 리스트를 순회하며 각 기술에 대해 ProjectTech 엔티티 생성
		List<ProjectTech> projectTechList = projectRequestDto.getTechsNo().stream()
			.map( techNo -> {
				// 각 techNo로 Tech 엔티티 조회
				Tech tech = techRepository.findById(techNo).orElseThrow(() -> new ProjectException(ExceptionMessage.TECH_NOT_FOUND));

				// ProjectTech 엔티티 생성: project.getNo()를 사용하여 project_no를 할당
				return ProjectTech.builder()
									.tech(tech)
									.project(project)
									.build();
			})
			.collect(Collectors.toList());


		// 4. 생성한 ProjectTech 엔티티들을 한 번에 저장
		projectTechRepository.saveAll(projectTechList);

		// 5. 프로젝트에도 프로젝트 리스트 저장해주기
		project.addProjectTechList(projectTechList);

		// 6. ProjectResponseDto 생성 후 반환
		return new ProjectResponseDto(project);
	}


	@Override
	@Transactional
	public ProjectResponseDto updateProject(Long projectNo, ProjectStatus projectStatus, ProjectUpdateRequestDto projectRequestDto) {

		CustomUserDetails userDetails = authService.getCurrentUser();


		// 1. 프로젝트 있는지 검증
		// 수정하고자 하는 프로젝트
		Project project = projectRepository.findById(projectNo).orElseThrow(
			() -> new ProjectException(ExceptionMessage.PROJECT_NOT_FOUND)
		);

		Team team = project.getTeam();


		// 3. user 가 team 에 속하는가
		boolean existsByUserNoAndTeamNo = teamUserRepository.existsByUserNoAndTeamNo(userDetails.getNo(), team.getNo());

		if (!existsByUserNoAndTeamNo) {
			throw new UserException(ExceptionMessage.USER_ACCESS_DENIED);
			// "사용자는 해당 프로젝트를 수정할 권한이 없습니다."
		}

		// 4. 기존 ProjectTech 리스트 삭제 (중복 데이터 방지)
		deleteProjectTechs(project);


		// 5. 새로운 ProjectTech 리스트 생성

		List<ProjectTech> newProjectTechList = projectRequestDto.getTechsNo().stream()
			.distinct() // 중복 제거
			.map(techNo -> {
				Tech tech = techRepository.findById(techNo).orElseThrow(() -> new TeamException(ExceptionMessage.TEAM_NOT_FOUND));



												return ProjectTech.builder()
																	.tech(tech)
																	.project(project)
																	.build();
											})
											.collect(Collectors.toList());

		// 6. 프로젝트에 새로운 프로젝트 기술 리스트 설정
		project.getProjectTeches().clear(); // 기존 리스트를 비움
		project.getProjectTeches().addAll(newProjectTechList); // 새로운 리스트 추가

		// 7. 프로젝트 업데이트 수행
		project.update(projectStatus, projectRequestDto);

		// 8. 업데이트된 프로젝트 정보 반환
		return new ProjectResponseDto(project);
	}


	@Transactional
	public void deleteProjectTechs(Project project) {
		projectTechRepository.deleteAllByProject(project);
		projectTechRepository.flush(); // DB 반영 보장
	}

	//  모든 프로젝트 조회
	@Override
	public Page<ProjectResponseDto> getAllProjects(Pageable pageable, ProjectSortOption projectSortOption) {
		return projectRepository.getProjects(pageable, projectSortOption);
	}


	// 프로젝트 단건 조회
	@Override
	@Transactional
	public ProjectResponseDto getProjectByProjectNo(Long projectNo) {

		// 1. 프로젝트가 존재하는지 조회
		Project project = projectRepository.findById(projectNo).orElseThrow(
			() -> new ProjectException(ExceptionMessage.PROJECT_NOT_FOUND)
		);


		return new ProjectResponseDto(project);
	}


	// 유저가 참여한 모든 프로젝트 표시
	@Override
	@Transactional(readOnly = true)
	public Page<ProjectResponseDto> getProjectsByUserNo(Long userNo, Pageable pageable) {

		// 1. 해당하는 유저
		User user = userRepository.findById(userNo).orElseThrow(
			() -> new UserException(ExceptionMessage.USER_NOT_FOUND, "ID: " + userNo)
		);

		// 2. user의 project list 가져오기
		Page<ProjectResponseDto> projectList = projectRepository.findProjectsByUserId(userNo, pageable);
		return projectList;
	}


	// 검색 결과 페이징 조회
	@Override
	@Transactional(readOnly = true)
	public Page<ProjectResponseDto> searchProject(String keyword, ProjectSearchOption searchOption, Pageable pageable) {

		// 검증할 게 없음. 바로 검색결과 리턴해주기
		Page<ProjectResponseDto> projectSearchList = projectRepository.searchProject(keyword, searchOption, pageable);

		return projectSearchList;
	}

	@Override
	@Transactional
	public void deleteProject(Long projectNo) {

		CustomUserDetails userDetails = authService.getCurrentUser();

		// 2. project 검증
		Project project = projectRepository.findById(projectNo).orElseThrow(
			() -> new ProjectException(ExceptionMessage.PROJECT_NOT_FOUND)
		);

		if ( authService.isAdmin() ){
			projectRepository.deleteById(projectNo);
			return;
		}

		Team team = project.getTeam();

		// 3. user 가 team 에 속하는가
		boolean existsByUserNoAndTeamNo = teamUserRepository.existsByUserNoAndTeamNo(userDetails.getNo(), project.getTeam().getNo());
		if (!existsByUserNoAndTeamNo) {
			throw new UserException(ExceptionMessage.USER_ACCESS_DENIED);
		}

		//  4. 리더가 아니면 예외
		if (!teamUserRepository.isLeader(team.getNo(), userDetails.getNo())) {
			throw new UserException(ExceptionMessage.USER_ACCESS_DENIED);
		}
		// 5. 프로젝트 삭제
		projectRepository.deleteById(projectNo);
	}

	/**
	 * 게시글 조회 시 조회수를 증가시키는 메서드
	 * - Redis를 활용하여 동일 사용자의 중복 조회를 방지(24시간 내)
	 */
	@Override
	@Transactional
	public void viewProject(Long projectNo, HttpServletRequest request) {
		// Redis에 저장할 고유 키 생성 (게시글ID + 사용자ID 조합)
		String key = "project:view:" + projectNo + ":"  + getUserId(request);

		// Redis에 키가 존재하지 않을 경우에만 값 설정 (24시간 유효)
		Boolean isNotViewed = redisTemplate.opsForValue().setIfAbsent(key, "Viewed", Duration.ofHours(24));
		// 첫 조회인 경우에만 조회수 증가 처리
		if (Boolean.TRUE.equals(isNotViewed)) {
			Project project = projectRepository.findById(projectNo)
					.orElseThrow(() -> new ProjectException(ExceptionMessage.PROJECT_NOT_FOUND, "ID: " + projectNo));
			projectRepository.increaseViewCount(projectNo);
		}
	}

	/**
	 * 사용자 또는 방문자의 고유 식별자를 생성하는 메서드
	 * - 로그인 사용자: 회원 번호 기반 해시값 생성
	 * - 비로그인 사용자: IP주소와 User-Agent 기반 해시값 생성
	 */
	private String getUserId(HttpServletRequest request) {
		String userIdentifier = "";
		CustomUserDetails userDetails = authService.getCurrentUser();

		// 로그인 된 사용자인 경우(회원)
		if (userDetails != null) {
			User user = userDetails.getUser();
			if (user != null) {
				//사용자 번호를 해시값으로 변환하여 식별자 생성
				userIdentifier = "user:" + user.getNo().hashCode();
				log.info("{} 님이 조회함", user.getUsername());
			}
		} else { //비로그인 사용자인 경우(게스트)
			//IP 주소 가져오기
			String ipAddress = request.getRemoteAddr();


			if (ipAddress != null && !ipAddress.isEmpty()) {
				// X-Forwarded-For 헤더가 있는 경우, 첫 번째 IP(클라이언트 실제 IP) 사용
				ipAddress = ipAddress.split(",")[0].trim();
			} else {
				// 헤더가 없는 경우 직접 연결된 IP 사용
				ipAddress = request.getRemoteAddr();
			}

			//User-Agent 정보 가져오기
			String userAgent = request.getHeader("User-Agent");

			//User-Agent 정보가 없는 경우 IP만으로 식별자 생성
			if (userAgent == null || userAgent.isEmpty()) {
				userIdentifier = "guest:" + ipAddress.hashCode();
			} else {//IP와 User-Agent를 조합하여 더 고유한 식별자 생성
				String identifier = ipAddress + userAgent;
				userIdentifier = "guest:" + (long) identifier.hashCode();
			}
			log.info("ip : {} User-Agent : {}인 게스트가 조회함", ipAddress, userAgent);

		}

		return userIdentifier;
	}


	/**
	 * 프로젝트를 끝내는 로직ㅠ
	 * 1. 프로젝트 상태 변경
	 * 2. 유저 평가 설문 날아감
	 * 3. 각각의 유저에게 설문 반영됨
	 */
}

