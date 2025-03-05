package com.beyond.backend.data.repository;

import com.beyond.backend.data.dto.postDto.PostResponseDto;
import com.beyond.backend.data.dto.postDto.UserPostResponseDto;
import com.beyond.backend.data.entity.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * <p>
 *
 * <p>packageName    : com.beyond.backend.data.repository
 * <p>fileName       : BookMarkRepositoryCustom
 * <p>author         : hyunjo
 * <p>date           : 25. 3. 2.
 * <p>description    :
 */
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 3. 2.        hyunjo             최초 생성
 */public interface BookMarkRepositoryCustom {

    Page<UserPostResponseDto> getBookmarkedPosts(Long userNo, BoardType boardType,Pageable pageable);
}