package com.beyond.backend.domain.post.repository;

import static com.beyond.backend.domain.post.entity.QPost.*;
import static com.beyond.backend.domain.user.entity.QUser.*;

import java.util.List;

import com.beyond.backend.domain.post.dto.UserPostResponseDto;
import com.beyond.backend.domain.post.entity.PostSearchOption;
import com.beyond.backend.domain.post.entity.PostStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.beyond.backend.domain.post.dto.PostResponseDto;
import com.beyond.backend.domain.post.entity.BoardType;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;


/**
 * <p>
 *
 * <p>packageName    : com.beyond.backend.data.repository.impl
 * <p>fileName       : PostRepositoryImpl
 * <p>author         : hyunjo
 * <p>date           : 25. 2. 20.
 * <p>description    :
 */
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 2. 20.        hyunjo             최초 생성
 */@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;
	

	@Override
    public Page<PostResponseDto> getPostsByBoardType(BoardType boardType,  Pageable pageable) {
        List<PostResponseDto> content = queryFactory
                .select(Projections.constructor(PostResponseDto.class,
                 post.no,
                 post.postTitle,
                 post.postContent,
				user.no,
                user.username,
				post.postStatus,
                post.createdAt,
                post.updatedAt
				))
                .from(post)
			    .leftJoin(post.user, user)
                .where(post.boardType.eq(boardType), post.postStatus.eq(PostStatus.ACTIVE))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

		JPAQuery<Long> totalCount = queryFactory
			.select(post.count())
			.from(post)
			.leftJoin(post.user, user)
			.where(post.boardType.eq(boardType), post.postStatus.eq(PostStatus.ACTIVE));


        return PageableExecutionUtils.getPage(content,pageable, totalCount::fetchOne);
    }




	//--------------------------------------------------------------------


 // 검색
    @Override
    public Page<PostResponseDto> searchPosts(BoardType boardType, PostSearchOption option, String keyword, Pageable pageable) {

		    List<PostResponseDto> content = queryFactory

				.select(Projections.constructor(PostResponseDto.class,
						post.no,
						post.postTitle,
						post.postContent,
						user.no,
						user.username,
						post.postStatus,
						post.createdAt,
						post.updatedAt
				))
				.from(post)
				.leftJoin(post.user, user)
					.where(
							post.boardType.eq(boardType),
							post.postStatus.eq(PostStatus.ACTIVE),
							searchOptions(keyword, option)

					)
					.offset(pageable.getOffset())
					.limit(pageable.getPageSize())
					.fetch();

			JPAQuery<Long> totalCount = queryFactory
				.select(post.count())
				.from(post)
				.leftJoin(post.user, user)
				.where(post.boardType.eq(boardType),
						post.postStatus.eq(PostStatus.ACTIVE),
						searchOptions(keyword, option)

					);



		return PageableExecutionUtils.getPage(content,pageable, totalCount::fetchOne);
    }


	private BooleanExpression searchOptions(String keyword, PostSearchOption option) {

		// 옵션 값이 없으면 전체 조회
		if(option == null){

			return null;
		}

		if (option == PostSearchOption.CONTENT) {

			return post.postContent.contains(keyword);

		} else if (option == PostSearchOption.TITLE) {

			return post.postTitle.contains(keyword);

		} else if (option == PostSearchOption.USERNAME) {

			return user.username.contains(keyword);
		}

		// 기본 전체 조회
		return null;
	}

	
	
	//----------------------------
	
	// 미완성--------------------------------------------------------------

	// 유저가 작성한 게시글 전체 조회
	@Override
	public Page<UserPostResponseDto> getUserPosts(Long userNo, Pageable pageable) {


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
				.from(post)
				.leftJoin(post.user, user)
				.where(user.no.eq(userNo))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		JPAQuery<Long> totalCount = queryFactory
				.select(post.count())
				.from(post)
				.leftJoin(post.user, user)
				.where(user.no.eq(userNo));


		return PageableExecutionUtils.getPage(content,pageable, totalCount::fetchOne);
		
	}



}

