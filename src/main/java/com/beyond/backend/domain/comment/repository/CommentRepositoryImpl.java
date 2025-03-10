package com.beyond.backend.domain.comment.repository;

import com.beyond.backend.domain.comment.dto.CommentResponseDto;
import com.beyond.backend.domain.comment.repository.CommentRepositoryCustom;
import com.beyond.backend.domain.post.dto.PostResponseDto;
import com.beyond.backend.domain.post.dto.UserPostResponseDto;
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


    // 사용자가 작성한 댓글 전체 조회
    @Override
    public Page<CommentResponseDto> getUserComments(Long userNo, Pageable pageable) {


        List<CommentResponseDto> content = queryFactory
                .select(Projections.constructor(CommentResponseDto.class,
                        comment.no,
                        comment.content,
                        user.no,
                        user.username,
                        post.no,
                        comment.likeCount,
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
                .from(comment)
                .join(comment.user, user)
                .where(user.no.eq(userNo));


        return PageableExecutionUtils.getPage(content,pageable, totalCount::fetchOne);
    }


    // 게시글의 댓글 전체 조회
    @Override
    public Page<CommentResponseDto> getPostComments(Long postNo, Pageable pageable) {

        List<CommentResponseDto> content = queryFactory
                .select(Projections.constructor(CommentResponseDto.class,
                        comment.no,
                        comment.content,
                        user.no,
                        user.username,
                        post.no,
                        comment.likeCount,
                        comment.createdAt,
                        comment.updatedAt
                ))
                .from(comment)
                .join(comment.post, post)
                .join(comment.user, user)
                .where(post.no.eq(postNo))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> totalCount = queryFactory
                .select(comment.count())
                .from(comment)
                .join(comment.post, post)
                .join(comment.user, user)
                .where(comment.post.no.eq(postNo));


        return PageableExecutionUtils.getPage(content,pageable, totalCount::fetchOne);
    }

    
    // 사용자가 댓글 단 게시글 전체 조회
    @Override
    public Page<PostResponseDto> getUserCommentPosts(Long userNo, Pageable pageable) {

        List<PostResponseDto> content = queryFactory
                .select(Projections.constructor(PostResponseDto.class,
                        post.no,
                        post.postTitle,
                        post.postContent,
                        post.user.no,
                        post.user.username,
                        post.viewCount,
                        post.bookmarkCount,
                        post.commentCount,
                        post.postStatus,
                        post.createdAt,
                        post.updatedAt
                ))
                .from(post)
                .join(comment).on(comment.post.eq(post))
                .join(comment.user, user)
                .where(user.no.eq(userNo))
                .distinct() // 중복 게시글 제거
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 개수 조회 (중복 제거)
        JPAQuery<Long> totalCount = queryFactory
                .select(post.countDistinct()) // 중복 방지 countDistinct 사용
                .from(post)
                .join(comment).on(comment.post.eq(post))
                .join(comment.user, user)
                .where(user.no.eq(userNo));

        return PageableExecutionUtils.getPage(content, pageable, totalCount::fetchOne);
    }

}

