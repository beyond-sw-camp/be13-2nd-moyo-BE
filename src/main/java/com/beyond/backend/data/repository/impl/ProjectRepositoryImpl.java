package com.beyond.backend.data.repository.impl;

import static com.beyond.backend.data.entity.QProject.*;
import static com.beyond.backend.data.entity.QTeamUser.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.beyond.backend.data.entity.Project;
import com.beyond.backend.data.repository.ProjectRepositoryCustom;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	// 다대다 관계 : repo에서 두 번 find 하면 성능 비효율 -> QueryDsl 사용
	public List<Project> findProjectsByUserId(Long userNo) {
		return queryFactory
				.select(project)
				.from(project)
				.join(project.team).fetchJoin()
				.where(project.team.no.in (
						JPAExpressions.select(teamUser.team.no)
						.from(teamUser)
						.where(teamUser.user.no.eq(userNo)) )
				)
				.fetch();
	}
}
