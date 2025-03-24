package com.beyond.backend.domain.post.service;

import com.beyond.backend.domain.post.dto.UserPostResponseDto;
import com.beyond.backend.domain.post.entity.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookMarkService {


    // 게시글 북마크 추가 / 취소
    String checkBookMark(Long postNo, Long userNo);

    // 유저가 북마크한 게시글 전체 조회
    Page<UserPostResponseDto> getBookmarkedPosts(BoardType boardType, Pageable pageable, Long userNo);



}
