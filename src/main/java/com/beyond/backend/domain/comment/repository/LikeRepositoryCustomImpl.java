package com.beyond.backend.domain.comment.repository;

import com.beyond.backend.domain.comment.dto.CommentResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.beyond.backend.domain.comment.entity.QComment.comment;
import static com.beyond.backend.domain.comment.entity.QLike.like;
import static com.beyond.backend.domain.post.entity.QPost.*;
import static com.beyond.backend.domain.user.entity.QUser.*;


/**
 * <p>
 *
 * <p>packageName    : com.beyond.backend.domain.like.repository
 * <p>fileName       : LikeRepositoryCustomImpl
 * <p>author         : hyunjo
 * <p>date           : 25. 3. 6.
 * <p>description    :
 */
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 3. 6.        hyunjo             최초 생성
 */
@Repository
@RequiredArgsConstructor
public class LikeRepositoryCustomImpl implements LikeRepositoryCustom {


    private final JPAQueryFactory queryFactory;

    // 내가 좋아요한 댓글
    @Override
    public Page<CommentResponseDto> getUserLikedComments(Long userNo, Pageable pageable) {


        List<CommentResponseDto> content = queryFactory

                .select(Projections.constructor(CommentResponseDto.class,

                        comment.no,
                        comment.content,
                        comment.user.no, // 댓글 작성자
                        comment.user.username, // 댓글 작성자
                        post.no,
                        comment.likeCount,
                        comment.createdAt,
                        comment.updatedAt

                        ))
                        .from(like)
                        .join(like.comment,comment)
                        .join(like.user, user)
                        .where(like.user.no.eq(userNo))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

                JPAQuery<Long> totalCount = queryFactory
                        .select(like.count())
                        .from(like)
                        .join(like.comment,comment)
                        .join(like.user, user)
                        .where(like.user.no.eq(userNo));

        return PageableExecutionUtils.getPage(content,pageable, totalCount::fetchOne);
    }

}
