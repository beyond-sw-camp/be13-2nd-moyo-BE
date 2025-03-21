package com.beyond.backend.domain.user.repository;

import static com.beyond.backend.domain.user.entity.QUser.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.beyond.backend.domain.user.dto.AllUserResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jdk.jfr.Registered;

/*
import static com.beyond.backend.domain.user.entity.QUser.*;
*/

@Registered
@Repository
public class UserRepositoryImpl implements UserRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    ///  모든 USER 조회
    /// ? : user 정렬 필요 없겟지
    public Page<AllUserResponseDto> getUsers(Pageable pageable) {

        List<AllUserResponseDto> users = queryFactory
                .select(Projections.constructor(AllUserResponseDto.class,
                        user.no,
                        user.username,
                        user.role,
                        user.email,
                        user.banned
                ))
                .from(user)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> totalCount = queryFactory
                .select(user.count())
                .from(user);

        return PageableExecutionUtils.getPage(users, pageable, totalCount::fetchOne);
    }
}
