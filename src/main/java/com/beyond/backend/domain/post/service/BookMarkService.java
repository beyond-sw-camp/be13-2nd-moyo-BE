package com.beyond.backend.domain.post.service;

import com.beyond.backend.domain.post.dto.UserPostResponseDto;
import com.beyond.backend.domain.post.entity.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookMarkService {


    String checkBookMark(Long postNo);

    Page<UserPostResponseDto> getBookmarkedPosts(BoardType boardType, Pageable pageable);



}
