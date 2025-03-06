package com.beyond.backend.domain.comment.repository;

import com.beyond.backend.domain.comment.dto.CommentResponseDto;
import com.beyond.backend.domain.comment.repository.CommentRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.beyond.backend.domain.comment.entity.QComment.*;
import static com.beyond.backend.domain.post.entity.QPost.*;
import static com.beyond.backend.domain.user.entity.QUser.*;

/**
 * <p>
 *
 * <p>packageName    : com.beyond.backend.data.repository
 * <p>fileName       : CommentRepositoryImpl
 * <p>author         : hyunjo
 * <p>date           : 25. 3. 5.
 * <p>description    :
 */
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 3. 5.        hyunjo             최초 생성
 */
@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {


    private final JPAQueryFactory queryFactory;

    private Long commentNo;
    private String content;
    private Long userNo; // userNo를 돌려줘서 나중에 로그인한 회원 비교로 사용할 예정
    private String userName;
    private Long postNo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Override
    public Page<CommentResponseDto> getUserComments(Long userNo, Pageable pageable) {


        List<CommentResponseDto> content = queryFactory
                .select(Projections.constructor(CommentResponseDto.class,
                        comment.no,
                        comment.content,
                        user.no,
                        user.username,
                        post.no,
                        comment.createdAt,
                        comment.updatedAt
                ))
                .from(comment)
                .join(comment.user, user)
                .where(user.no.eq(userNo))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> totalCount = queryFactory
                .select(comment.count())
                .from(post)
                .join(comment.user, user)
                .where(user.no.eq(userNo));


        return PageableExecutionUtils.getPage(content,pageable, totalCount::fetchOne);
    }
}

