package com.beyond.backend.data.repository.impl;

import com.beyond.backend.data.dto.postDto.PostResponseDto;
import com.beyond.backend.data.dto.postDto.UserPostResponseDto;
import com.beyond.backend.data.entity.BoardType;
import com.beyond.backend.data.entity.PostStatus;

import com.beyond.backend.data.entity.Status;
import com.beyond.backend.data.repository.BookMarkRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import static com.beyond.backend.data.entity.QPost.*;
import static com.beyond.backend.data.entity.QUser.*;
import static com.beyond.backend.data.entity.QBookMark.*;


import java.util.List;

/**
 * <p>
 *
 * <p>packageName    : com.beyond.backend.data.repository.impl
 * <p>fileName       : BookMarkRepositoryImpl
 * <p>author         : hyunjo
 * <p>date           : 25. 3. 2.
 * <p>description    :
 */
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 3. 2.        hyunjo             최초 생성
 */
@Repository
@RequiredArgsConstructor
public class BookMarkRepositoryImpl implements BookMarkRepositoryCustom {


     private final JPAQueryFactory queryFactory;


    @Override
    public Page<UserPostResponseDto> getBookmarkedPosts(Long userNo, BoardType boardType, Pageable pageable) {


        List<UserPostResponseDto> content = queryFactory
                .select(Projections.constructor(UserPostResponseDto.class,

                        post.no,
                        post.postTitle,
                        post.postContent,
                        user.no,
                        user.username,
                        post.boardType,
                        post.postStatus,
                        post.createdAt,
                        post.updatedAt
                ))
                .from(bookMark)
                .join(bookMark.post, post)
                .join(bookMark.user, user)
                .where(

                        bookMark.user.no.eq(userNo),
                        boardType(boardType)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();




        JPAQuery<Long> totalCount = queryFactory
                .select(post.count())
                .from(bookMark)
                .join(bookMark.post, post)
                .where(
                        bookMark.user.no.eq(userNo)
                       , boardType(boardType)
                );

        return PageableExecutionUtils.getPage(content, pageable, totalCount::fetchOne);


    }

    private BooleanExpression boardType(BoardType boardType) {

        if (boardType == null) {

            return null;
        }

        return post.boardType.eq(boardType);
    }
}