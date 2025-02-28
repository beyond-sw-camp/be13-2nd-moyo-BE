package com.beyond.backend.data.repository;

import com.beyond.backend.data.dto.postDto.PostResponseDto;
import com.beyond.backend.data.entity.BoardType;
import com.beyond.backend.data.entity.SearchOption;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

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

    Page<PostResponseDto> searchPosts(BoardType boardType, SearchOption option, String keyword, Pageable pageable);
}