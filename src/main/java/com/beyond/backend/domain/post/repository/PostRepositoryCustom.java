package com.beyond.backend.domain.post.repository;

import com.beyond.backend.domain.post.dto.PostResponseDto;
import com.beyond.backend.domain.post.dto.UserPostResponseDto;
import com.beyond.backend.domain.post.entity.BoardType;
import com.beyond.backend.domain.post.entity.Post;
import com.beyond.backend.domain.post.entity.PostSearchOption;

import com.beyond.backend.domain.post.entity.PostSortOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * <p>
 *
 * <p>packageName    : com.beyond.backend.data.repository
 * <p>fileName       : PostRepositoryCustom
 * <p>author         : hyunjo
 * <p>date           : 25. 2. 20.
 * <p>description    :
 */
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 2. 20.        hyunjo             최초 생성
 */public interface PostRepositoryCustom {
    //Page<PostResponseDto> getPostsByBoardType(BoardType boardType, Pageable pageable, PostSortOption postSortOption);

    // 게시글 검색 + 전체 조회
    Page<PostResponseDto> searchPosts(BoardType boardType, PostSearchOption option, String keyword, Pageable pageable, PostSortOption postSortOption);

    // 유저가 작성한 게시글 전체 조회
    Page<UserPostResponseDto> getUserPosts(Long userNo, BoardType boardType, Pageable pageable);

}
