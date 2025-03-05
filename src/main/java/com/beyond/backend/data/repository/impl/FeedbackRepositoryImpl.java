package com.beyond.backend.data.repository.impl;

import static com.beyond.backend.data.entity.QFeedback.*;
import static com.beyond.backend.data.entity.QProject.*;
import static com.beyond.backend.data.entity.QUser.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.beyond.backend.data.dto.FeedbackResponseDto;
import com.beyond.backend.data.repository.FeedbackRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class FeedbackRepositoryImpl implements FeedbackRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	public FeedbackRepositoryImpl(EntityManager em){
		this.queryFactory= new JPAQueryFactory(em);
	}

	// 1. 내가 쓴 피드백 조회
	@Override
	public Page<FeedbackResponseDto> findFeedbackByUserId(Long userNo, Pageable pageable) {

		List<FeedbackResponseDto> feedbackList = queryFactory
			.select(Projections.constructor(FeedbackResponseDto.class,
				feedback.no,
				feedback.content,
				feedback.feedbackType,
				user.name,
				project.name
			))
			.from(feedback)
			.join(feedback.user, user)
			.join(feedback.project, project)
			.where(user.no.eq(userNo))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> totalCount = queryFactory
			.select(feedback.count())
			.from(feedback)
			.where(feedback.user.no.eq(userNo));

		return PageableExecutionUtils.getPage(feedbackList, pageable, totalCount::fetchOne);
	}

	// 2. 팀 프로젝트 에서 피드백 조회
	@Override
	public Page<FeedbackResponseDto> findFeedbackByProjectId(Long projectNo, Pageable pageable) {
		List<FeedbackResponseDto> feedbackList = queryFactory
			.select(Projections.constructor(FeedbackResponseDto.class,
				feedback.no,
				feedback.content,
				feedback.feedbackType,
				user.name,
				project.name
			))
			.from(feedback)
			.join(feedback.user, user)
			.join(feedback.project, project)
			.where(project.no.eq(projectNo))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> totalCount = queryFactory
			.select(feedback.count())
			.from(feedback)
			.where(feedback.project.no.eq(projectNo));

		return PageableExecutionUtils.getPage(feedbackList, pageable, totalCount::fetchOne);
	}
}
