
package com.beyond.backend.domain.project.repository;


import com.beyond.backend.domain.project.dto.ProjectResponseDto;
import com.beyond.backend.domain.project.entity.ProjectSearchOption;
import com.beyond.backend.domain.project.entity.ProjectSortOption;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.beyond.backend.domain.project.entity.QProject.project;
import static com.beyond.backend.domain.project.entity.QProjectTech.projectTech;
import static com.beyond.backend.domain.team.entity.QTeam.team;
import static com.beyond.backend.domain.teamUser.entity.QTeamUser.teamUser;

/*import static com.beyond.backend.domain.project.entity.QProject.project;
import static com.beyond.backend.domain.project.entity.QProjectTech.projectTech;
import static com.beyond.backend.domain.team.entity.QTeam.team;
import static com.beyond.backend.domain.teamUser.entity.QTeamUser.teamUser;*/

@Slf4j
@Repository
public class ProjectRepositoryImpl implements ProjectRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	// 생성자 주입 권장 방식
	public ProjectRepositoryImpl(EntityManager em){
		this.queryFactory = new JPAQueryFactory(em);
	}

	///  모든 프로젝트 조회 - 최신순, 조회순
	@Override
	public Page<ProjectResponseDto> getProjects(Pageable pageable, ProjectSortOption projectSortOption) {

		// 1. 모든 프로젝트 기본 정보 조회
		List<ProjectResponseDto> allProjects = queryFactory
			.select(Projections.constructor(ProjectResponseDto.class,
				project.no,
				project.name,
				team.teamName,
				project.content,
				project.viewCnt,
				project.projectStatus
			))
			.from(project)
			.join(project.team, team)
			.orderBy(getOrderSpecifier(projectSortOption)) // 최신순, 조회순
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();


		getMatchProjectAndTech(allProjects);


		// 전체 프로젝트 개수 조회
		JPAQuery<Long> totalCount = queryFactory
			.select(project.count())
			.from(project);


		return PageableExecutionUtils.getPage(allProjects, pageable, totalCount::fetchOne);
	}


	/// 정렬 (Pageable Sort 활용)
	private OrderSpecifier<?> getOrderSpecifier(ProjectSortOption projectSortOption){


		if(projectSortOption == null){
			return project.createdAt.desc();
		}

		switch (projectSortOption){
			case LATEST :
				return project.createdAt.desc();
			case VIEW:
				return project.viewCnt.desc();
			// default = 최신순
			default:
				return project.createdAt.desc();
		}

	}


	///  프로젝트 검색 조회
	@Override
	public Page<ProjectResponseDto> searchProject(String keyword, ProjectSearchOption option, Pageable pageable) {

		List<ProjectResponseDto> searchList = queryFactory
			.select(Projections.constructor( ProjectResponseDto.class,
				project.no,
				project.name,
				team.teamName,
				project.content,
				project.viewCnt,
				project.projectStatus
			))
			.from(project)
			.leftJoin(project.team, team)
			.where(searchOption(keyword, option))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		getMatchProjectAndTech(searchList);

		JPAQuery<Long> totalCount = queryFactory
			.select( project.count() )
			.from(project);


		return PageableExecutionUtils.getPage(searchList, pageable, totalCount::fetchOne);
	}


	/// 프로젝트 Id 로 프로젝트 Tech 가져오기
	public Map<Long, List<String>> getProjectTechs(List<Long> projectIds) {
		if (projectIds == null || projectIds.isEmpty()) {
			return new HashMap<>(); // 프로젝트가 없으면 빈 맵 반환
		}

		return queryFactory
			.select(projectTech.project.no, projectTech.tech.techName)
			.from(projectTech)
			.where(projectTech.project.no.in(projectIds))
			.fetch()
			.stream()
			.collect(Collectors.groupingBy(
				tuple -> tuple.get(projectTech.project.no), // teamNo 기준으로 그룹화
				Collectors.mapping(tuple -> tuple.get(projectTech.tech.techName), Collectors.toList()) // techName 리스트 매핑
			));
	}


	private BooleanExpression searchOption(String keyword, ProjectSearchOption option){

		// 옵션이 없다면 전체 option에서 contains 하기
		if (option == null){
			return project.name.contains(keyword)
				.or(project.content.contains(keyword))
				.or(projectExistsInTech(keyword));

		}

		return switch(option){
			case NAME -> project.name.contains(keyword);
			case CONTENT -> project.content.contains(keyword);
			case PROJECT_TECHS -> projectExistsInTech(keyword); // 서브쿼리 활용
		};
	}



	private BooleanExpression projectExistsInTech(String keyword) {

		return JPAExpressions.selectOne()
			.from(projectTech)
			.where(
				projectTech.project.no.eq(project.no)
					.and(projectTech.tech.techName.contains(keyword))
			)
			.exists();
	}


	/// 유저가 참여한 모든 프로젝트 조회
	public Page<ProjectResponseDto> findProjectsByUserId(Long userNo, Pageable pageable) {

		List<ProjectResponseDto> projectList = queryFactory
			.select(Projections.constructor(ProjectResponseDto.class,
				project.no,
				project.name,
				team.teamName,
				project.content,
				project.viewCnt,
				project.projectStatus
			))
			.from(teamUser)
			.join(teamUser.team, team) // 팀user <-> team 조인
			.join(team.project, project) // team <-> project 조인
			.where(teamUser.user.no.eq(userNo))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();


		getMatchProjectAndTech(projectList);

		// 한 유저가 참여한 프로젝트 개수
		JPAQuery<Long> totalCount = queryFactory
			.select(project.no.countDistinct())
			.from(project)
			.join(project.team, team)
			.join(team.teamUsers , teamUser)
			.where(teamUser.user.no.eq(userNo));

		return PageableExecutionUtils.getPage(projectList, pageable, totalCount::fetchOne);
	}


	private void getMatchProjectAndTech(List<ProjectResponseDto> projectList){

		// 1. 프로젝트 ID 리스트 추출
		List<Long> projectNoList = projectList.stream()
			.map(ProjectResponseDto::getNo)
			.toList();

		// 2. 프로젝트 기술 스택 조회 후 매핑
		Map<Long, List<String>> techList = getProjectTechs(projectNoList);

		// 3. 각 프로젝트에 기술 스택 리스트 추가
		projectList.forEach(proj ->
			proj.setProjectTeches(techList.getOrDefault(proj.getNo(), List.of()))
		);

	}


}

