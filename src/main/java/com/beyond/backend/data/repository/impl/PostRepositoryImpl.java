package com.beyond.backend.data.repository.impl;

import static com.beyond.backend.data.entity.QPost.*;
import static com.beyond.backend.data.entity.QUser.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.beyond.backend.data.dto.postDto.PostResponseDto;
import com.beyond.backend.data.entity.BoardType;
import com.beyond.backend.data.entity.Post;
import com.beyond.backend.data.entity.SearchOption;
import com.beyond.backend.data.entity.Status;
import com.beyond.backend.data.repository.PostRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;



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

public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

	public PostRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
    public Page<PostResponseDto> getPostsByBoardType(BoardType boardType,  Pageable pageable) {
        List<PostResponseDto> content = queryFactory
                .select(Projections.constructor(PostResponseDto.class,
                 post.no,
                 post.postTitle,
                 post.postContent,
                user.username,
                post.createdAt,
                post.updatedAt
				))
                .from(post)
			    .leftJoin(post.user, user).fetchJoin()
                .where(post.boardType.eq(boardType), post.status.eq(Status.ACTIVE))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

		JPAQuery<Long> totalCount = queryFactory
			.select(post.count())
			.from(post)
			.leftJoin(post.user, user)
			.where(post.boardType.eq(boardType), post.status.eq(Status.ACTIVE));


        return PageableExecutionUtils.getPage(content,pageable, totalCount::fetchOne);
    }





	//--------------------------------------------------------------------



    @Override
    public Page<PostResponseDto> searchPosts(BoardType boardType, SearchOption option, String keyword, Pageable pageable) {
		List<PostResponseDto> content = queryFactory
			.select(Projections.constructor(PostResponseDto.class,
				post.no,
				post.postTitle,
				post.postContent,
				user.username,
				post.createdAt,
				post.updatedAt
			))
			.from(post)
			.leftJoin(post.user, user).fetchJoin()
                .where(
                        post.boardType.eq(boardType),
                        post.status.eq(Status.ACTIVE)

						// 옵션 값이 넘어온 경우 해당 옵션에 해당하는 게시글 제목, 내용, 작성자 조회

					// 옵션 값이 null이면 전체 조회

                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

            JPAQuery<Long> totalCount = queryFactory
                .select(post.count())
                .from(post)
                .leftJoin(post.user, user)
                .where(post.boardType.eq(boardType),
					post.status.eq(Status.ACTIVE

					));



		return PageableExecutionUtils.getPage(content,pageable, totalCount::fetchOne);
    }
}

/*
private BooleanExpression conditionEq(SearchOption option, String keyword) {
	return option == null ? post.postTitle.containsIgnoreCase(keyword)
		.or(post.postContent.containsIgnoreCase(keyword))
		.or(post.user.username.containsIgnoreCase(keyword))
		// 아니면 해당 검색 옵션의 조회
		:
	// username이 null 또는 빈 문자열("")이면 WHERE 절에서 제외
	// username이 존재하면 WHERE member.username = '입력값'
}
*/


/*
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    // 회원 검색 메서드 (회원명, 팀명, 나이 조건 적용)
    @Override
    public List<MemberTeamDto> search(MemberSearchCondition condition) {
        return queryFactory
            .select(new QMemberTeamDto( // DTO 매핑을 위해 Q클래스 사용
                member.id,
                member.username,
                member.age,
                team.id,
                team.name
            ))
            .from(member) // member 테이블을 조회
            .leftJoin(member.team, team) // member와 team을 LEFT JOIN
            .where( // 동적 검색 조건 적용 (BooleanExpression 사용)
                usernameEq(condition.getUsername()),
                teamNameEq(condition.getTeamName()),
                ageGoe(condition.getAgeGoe()),
                ageLoe(condition.getAgeLoe())
            )
            .fetch(); // 쿼리를 실행하고 결과를 리스트로 반환
    }

    // 회원명(username) 검색 조건을 반환하는 메서드
    private BooleanExpression usernameEq(String username) {
        return isEmpty(username) ? null : member.username.eq(username);
        // username이 null 또는 빈 문자열("")이면 WHERE 절에서 제외
        // username이 존재하면 WHERE member.username = '입력값'
    }

    // 팀명(teamName) 검색 조건을 반환하는 메서드
    private BooleanExpression teamNameEq(String teamName) {
        return isEmpty(teamName) ? null : team.name.eq(teamName);
        // teamName이 null 또는 빈 문자열("")이면 WHERE 절에서 제외
        // teamName이 존재하면 WHERE team.name = '입력값'
    }

    // 최소 나이(ageGoe, greater or equal) 조건을 반환하는 메서드
    private BooleanExpression ageGoe(Integer ageGoe) {
        return ageGoe == null ? null : member.age.goe(ageGoe);
        // ageGoe가 null이면 WHERE 절에서 제외
        // ageGoe가 존재하면 WHERE member.age >= 입력값
    }

    // 최대 나이(ageLoe, less or equal) 조건을 반환하는 메서드
    private BooleanExpression ageLoe(Integer ageLoe) {
        return ageLoe == null ? null : member.age.loe(ageLoe);
        // ageLoe가 null이면 WHERE 절에서 제외
        // ageLoe가 존재하면 WHERE member.age <= 입력값
    }
}
 */
