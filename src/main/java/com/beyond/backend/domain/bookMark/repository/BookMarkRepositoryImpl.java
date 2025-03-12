package com.beyond.backend.domain.bookMark.repository;

import com.beyond.backend.domain.post.dto.UserPostResponseDto;
import com.beyond.backend.domain.post.entity.BoardType;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

/*import static com.beyond.backend.domain.post.entity.QPost.*;
import static com.beyond.backend.domain.user.entity.QUser.*;
import static com.beyond.backend.domain.bookMark.entity.QBookMark.*;*/


import java.util.List;

import static com.beyond.backend.domain.bookMark.entity.QBookMark.bookMark;
import static com.beyond.backend.domain.post.entity.QPost.post;
import static com.beyond.backend.domain.user.entity.QUser.user;

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
                        user.username, // 현재는 북마크한 유저 넘버가 같이 나옴
                        post.viewCount,
                        post.bookmarkCount,
                        post.commentCount,
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
                .select(bookMark.count())
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


    //--------------
    // 최신 북마크 개수 조회

    // 북마크 추가, 삭제 후 최신 북마크 개수
 /*   @Override
    public int getLatestBookmarkCount(Long postNo) {
        Integer bookmarkCount = queryFactory
                .select(post.bookmarkCount)
                .from(post)
                .where(post.no.eq(postNo))
                .fetchOne();

        // Null 처리 (데이터가 없을 경우 0 반환)
        return bookmarkCount != null ? bookmarkCount : 0;
    }

    // 북마크 개수 증가
    @Override
    @Transactional
    public int increaseBookmark(Long postNo) {
        long updatedCount = queryFactory
                .update(post)
                .set(post.bookmarkCount, post.bookmarkCount.add(1))
                .where(post.no.eq(postNo))
                .execute();

        return (int) updatedCount;
    }

    // 북마크 개수 감소
    @Override
    @Transactional
    public int decreaseBookmark(Long postNo) {
        long updatedCount = queryFactory
                .update(post)
                .set(post.bookmarkCount, post.bookmarkCount.subtract(1))
                .where(post.no.eq(postNo)
                        .and(post.bookmarkCount.gt(0))) // 0 이하로 내려가지 않도록 처리
                .execute();

        return (int) updatedCount;
    }*/
}