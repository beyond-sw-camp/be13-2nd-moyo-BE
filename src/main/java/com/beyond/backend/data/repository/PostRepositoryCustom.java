package com.beyond.backend.data.repository;

import com.beyond.backend.data.dto.postDto.PostResponseDto;
import com.beyond.backend.data.dto.postDto.UserPostResponseDto;
import com.beyond.backend.data.entity.BoardType;
import com.beyond.backend.data.entity.Post;
import com.beyond.backend.data.entity.PostSearchOption;

import com.beyond.backend.data.entity.PostStatus;
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
    Page<PostResponseDto> getPostsByBoardType(BoardType boardType, Pageable pageable);

    Page<PostResponseDto> searchPosts(BoardType boardType, PostSearchOption option, String keyword, Pageable pageable);

    Page<UserPostResponseDto> getUserPosts(Long userNo, Pageable pageable);

}
