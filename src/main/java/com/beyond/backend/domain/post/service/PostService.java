package com.beyond.backend.domain.post.service;

import com.beyond.backend.domain.comment.entity.CommentSortOption;
import com.beyond.backend.domain.post.dto.PostDto;
import com.beyond.backend.domain.post.dto.PostResponseDto;
import com.beyond.backend.domain.post.dto.UserPostResponseDto;
import com.beyond.backend.domain.post.entity.BoardType;
import com.beyond.backend.domain.post.entity.PostSearchOption;

import com.beyond.backend.domain.post.entity.PostSortOption;
import com.beyond.backend.domain.post.entity.PostStatus;
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


    // 게시글 검색 + 전체 조회
    Page<PostResponseDto> searchPosts(BoardType boardType, PostSearchOption option, PostSortOption postSortOption, String keyword, Pageable pageable);

    //게시글 단건 조회
    PostResponseDto getPostById(Long postNo);

    // 게시글 생성
    PostResponseDto createPost(BoardType boardType, PostDto postDto);

    // 게시글 수정
    PostResponseDto updatePost(PostStatus postStatus, Long postNo, PostDto postDto);

    // 게시글 삭제
    void deletePost(Long postN) ;

    // 내가 쓴 게시글 전체 조회
    Page<UserPostResponseDto> getUserPosts(BoardType boardType, Pageable pageable);

    // 공지 게시판인 경우 관리자인지 검증
    void validatePostAuthority(BoardType boardType);

}
