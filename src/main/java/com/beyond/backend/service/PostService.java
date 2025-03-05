package com.beyond.backend.service;

import com.beyond.backend.data.dto.postDto.PostDto;
import com.beyond.backend.data.dto.postDto.PostResponseDto;
import com.beyond.backend.data.dto.postDto.UserPostResponseDto;
import com.beyond.backend.data.entity.BoardType;
import com.beyond.backend.data.entity.PostSearchOption;

import com.beyond.backend.data.entity.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * <p>
 *
 * <p>packageName    : com.beyond.backend.service
 * <p>fileName       : PostService
 * <p>author         : hyunjo
 * <p>date           : 25. 2. 2.
 * <p>description    :
 */
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 2. 2.        hyunjo             최초 생성
 * 25. 2. 17.       hyunjo             내용 수정
 * 25. 2. 20.       hyunjo             내용 수정
 */public interface PostService {

     // 게시글 전체 조회
    Page<PostResponseDto> getPosts(BoardType boardType, Pageable pageable);

    // 게시글 검색
    Page<PostResponseDto> searchPosts(BoardType boardType, PostSearchOption option, String keyword, Pageable pageable);

    // 게시글 단 건 조횐
    PostResponseDto getPostById(Long postNo);


    // 게시글 생성
    PostResponseDto createPost(BoardType boardType, PostDto postDto);

    // 게시글 수정
    PostResponseDto updatePost(BoardType boardType,PostStatus postStatus, Long postNo, PostDto postDto);

    // 게시글 삭제
    void deletePost(Long postNo) ;


    // 내가 쓴 게시글 전체 조회
    Page<UserPostResponseDto> getUserPosts(Long userNo, Pageable pageable);

    //----

    // 게시글 북마크

    String checkBookMark(Long postNo, Long userNo);

    Page<UserPostResponseDto> getBookmarkedPosts(Long userNo, BoardType boardType, Pageable pageable);


    //----
}
