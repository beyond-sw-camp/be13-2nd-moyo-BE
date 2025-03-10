package com.beyond.backend.domain.user.repository;

import com.beyond.backend.domain.user.dto.UserGetResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jdk.jfr.Registered;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.beyond.backend.domain.user.entity.QUser.user;

/*
import static com.beyond.backend.domain.user.entity.QUser.*;
*/

@Registered
public class UserRepositoryImpl implements UserRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Page<UserGetResponseDto> getUsers(Pageable pageable) {
        List<UserGetResponseDto> users = queryFactory
                .select(Projections.constructor(UserGetResponseDto.class,
                        user.username,
                        user.password,
                        user.role
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