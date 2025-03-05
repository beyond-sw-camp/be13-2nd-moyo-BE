package com.beyond.backend.data.repository.impl;

import static com.beyond.backend.data.entity.QProject.*;
import static com.beyond.backend.data.entity.QProjectTech.*;
import static com.beyond.backend.data.entity.QTeam.*;
import static com.beyond.backend.data.entity.QTeamUser.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.beyond.backend.data.dto.ProjectResponseDto;
import com.beyond.backend.data.entity.ProjectSearchOption;
import com.beyond.backend.data.entity.ProjectTech;
import com.beyond.backend.data.entity.QProjectTech;
import com.beyond.backend.data.repository.ProjectRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ListPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ProjectRepositoryImpl implements ProjectRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	// 생성자 주입 권장 방식
	public ProjectRepositoryImpl(EntityManager em){
		this.queryFactory = new JPAQueryFactory(em);
	}

	///  모든 프로젝트 조회 -> 완료된 프로젝트만 조회하기
	///  회의!!!!!!!!!!!
	///  정렬 기능을 넣거라 !!!!!!!!! = 최신순, 조회순 ..........
	@Override
	public Page<ProjectResponseDto> getProjects(Pageable pageable) {

		// 1. 모든 프로젝트 기본 정보 조회
		List<ProjectResponseDto> allProjects = queryFactory
			.select(Projections.constructor(ProjectResponseDto.class,
				project.no,
				project.name,
				team.teamName,
				project.content,
				project.projectStatus
			))
			.from(project)
			.join(project.team, team)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();


		// 프로젝트 ID 추출
		List<Long> projectList = allProjects.stream().map(ProjectResponseDto::getNo).toList();

		// 2.  각 프로젝트에 대한 프로젝트 테크 목록 조회
		Map<Long, List<String>> projectTechMap = queryFactory
			.select(projectTech.tech.no, projectTech.tech.techName)
			.from(projectTech)
			.where(projectTech.projectId.in(projectList))
			.fetch()
			.stream()
			.collect(Collectors.groupingBy( // stream 요소들을 groupingBy 를 통해 Map 객체로 반환
				tuple -> tuple.get(projectTech.projectId), // projectId 를 Map의 key 값으로 그룹화
				Collectors.mapping(tuple -> tuple.get(projectTech.tech.techName), Collectors.toList()) // techName 리스트가 value
			));

		// 3. 각 프로젝트에 해당하는 projectTeches 설정
		allProjects.forEach(proj -> proj.setProjectTeches(projectTechMap.getOrDefault(proj.getNo(), new ArrayList<>())));


		// 4. 전체 프로젝트 개수 조회
		JPAQuery<Long> totalCount = queryFactory
			.select(project.count())
			.from(project);


		return PageableExecutionUtils.getPage(allProjects, pageable, totalCount::fetchOne);
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
				project.projectStatus
			))
			.from(project)
			.leftJoin(project.team, team)
			.where(searchOption(keyword, option))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> totalCount = queryFactory
				.select( project.count() )
				.from(project);


		return PageableExecutionUtils.getPage(searchList, pageable, totalCount::fetchOne);
	}

	private BooleanExpression searchOption(String keyword, ProjectSearchOption option){

		// 옵션이 없다면 전체 option에서 contains 하기
		if (option == null){
			return project.name.contains(keyword)
				.or(project.content.contains(keyword));
		}

		return switch(option){
			case NAME -> project.name.contains(keyword);
			case CONTENT -> project.content.contains(keyword);
			///  projectTech list<string>으로 바꿨으니까 수정해야 한다.
			case PROJECT_TECHS -> project.projectTeches.any().tech.techName.contains(keyword);
		};

	}

	/// 유저가 참여한 모든 프로젝트 조회
	public Page<ProjectResponseDto> findProjectsByUserId(Long userNo, Pageable pageable) {

		List<ProjectResponseDto> projectList = queryFactory
			.select(Projections.constructor(ProjectResponseDto.class,
				project.no,
				project.name,
				team.teamName,
				project.content,
				project.projectStatus
			))
			.from(teamUser)
			.join(teamUser.team, team) // 팀user <-> team 조인
			.join(team.projects, project) // team <-> project 조인
			.where(teamUser.user.no.eq(userNo))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		// user 의 프로젝트 no 리스트 생성
		List<Long> projectNoList = projectList.stream().map(ProjectResponseDto::getNo).toList();

		// 리스트 no 에 대응하는 projectTech 가져오기
		Map<Long, List<String>> techList = queryFactory
			.select(projectTech.tech.no, projectTech.tech.techName)
			.from(projectTech)
			.where(projectTech.projectId.in(projectNoList))
			.fetch()
			.stream()
			.collect(
				Collectors.groupingBy(
					tuple-> tuple.get(projectTech.projectId),
					Collectors.mapping(tuple -> tuple.get(projectTech.tech.techName), Collectors.toList() ))
			);

		projectList.forEach(proj -> proj.setProjectTeches(techList.getOrDefault(proj.getNo(), new ArrayList<>())));

		// 한 유저가 참여한 프로젝트 개수
		JPAQuery<Long> totalCount = queryFactory
			.select(project.no.countDistinct())
			.from(project)
			.join(project.team, team)
			.join(team.teamUsers , teamUser)
			.where(teamUser.user.no.eq(userNo));

		return PageableExecutionUtils.getPage(projectList, pageable, totalCount::fetchOne);
	}

}
