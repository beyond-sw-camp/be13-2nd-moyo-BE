package com.beyond.backend.domain.post.repository;

/*import static com.beyond.backend.domain.post.entity.QPost.*;
import static com.beyond.backend.domain.user.entity.QUser.*;*/

import com.beyond.backend.domain.post.dto.PostResponseDto;
import com.beyond.backend.domain.post.dto.UserPostResponseDto;
import com.beyond.backend.domain.post.entity.BoardType;
import com.beyond.backend.domain.post.entity.Post;
import com.beyond.backend.domain.post.entity.PostSearchOption;
import com.beyond.backend.domain.post.entity.PostSortOption;
import com.beyond.backend.domain.post.entity.PostStatus;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.beyond.backend.domain.post.entity.QPost.post;
import static com.beyond.backend.domain.user.entity.QUser.user;


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



	// 게시글 정렬 조건
	private OrderSpecifier<?>[] getOrderSpecifier(PostSortOption postSortOption) {
		if (postSortOption == null || postSortOption == PostSortOption.LATEST) {
			return new OrderSpecifier[]{
					post.createdAt.desc(),
					post.no.desc()
					// 최신순으로 정렬 시 시간이 같을 경우 인덱스 정렬 조건도 보조로 추가
			};
		}

		return switch (postSortOption) {
			case BOOKMARK -> new OrderSpecifier[]{ post.bookmarkCount.desc(), post.no.desc() };
			case VIEW     -> new OrderSpecifier[]{ post.viewCount.desc(), post.no.desc() };
			case COMMENT  -> new OrderSpecifier[]{ post.commentCount.desc(), post.no.desc() };
			default       -> new OrderSpecifier[]{ post.createdAt.desc(), post.no.desc() };
		};
	}

	// 검색 조건
	private BooleanExpression searchOptions(String keyword, PostSearchOption option) {

		// 옵션 값이 없으면 전체 조회(최신 순)
		// 검색어가 있으면 검색 옵션도 있어야 함
		if(option == null || keyword == null) {

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

    // 게시글 검색 + 전체 조회
    @Override
    public Page<PostResponseDto> searchPosts(BoardType boardType, PostSearchOption option, String keyword, Pageable pageable, PostSortOption postSortOption) {

		    List<PostResponseDto> content = queryFactory

				.select(Projections.constructor(PostResponseDto.class,
						post.no,
						post.postTitle,
						post.postContent,
						user.no,
						user.username,
						post.viewCount,
						post.bookmarkCount,
						post.commentCount,
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
					.orderBy(getOrderSpecifier(postSortOption))
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



	// 유저가 작성한 게시글 전체 조회 ( 활성, 비활성 상태 둘 다 가져옴 )
	@Override
	public Page<UserPostResponseDto> getUserPosts(Long userNo, BoardType boardType, Pageable pageable) {


		List<UserPostResponseDto> content = queryFactory
				.select(Projections.constructor(UserPostResponseDto.class,
						post.no,
						post.postTitle,
						post.postContent,
						user.no,
						user.username,
						post.viewCount,
						post.bookmarkCount,
						post.commentCount,
						post.boardType,
						post.postStatus,
						post.createdAt,
						post.updatedAt
				))
				.from(post)
				.leftJoin(post.user, user)
				.where( user.no.eq(userNo),
						post.boardType.eq(boardType)
				)
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		JPAQuery<Long> totalCount = queryFactory
				.select(post.count())
				.from(post)
				.leftJoin(post.user, user)
				.where(user.no.eq(userNo), post.boardType.eq(boardType));


		return PageableExecutionUtils.getPage(content,pageable, totalCount::fetchOne);
		
	}



}

