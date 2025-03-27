package com.beyond.backend.domain.post.service;

import com.beyond.backend.domain.post.dto.PostDto;
import com.beyond.backend.domain.post.dto.PostResponseDto;
import com.beyond.backend.domain.post.dto.UserPostResponseDto;
import com.beyond.backend.domain.post.entity.BoardType;
import com.beyond.backend.domain.post.entity.PostSearchOption;
import com.beyond.backend.domain.post.entity.PostSortOption;
import com.beyond.backend.domain.user.dto.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
 */
@Service
public interface PostService {


    // 게시글 검색 + 전체 조회
    Page<PostResponseDto> searchPosts(BoardType boardType, PostSearchOption option, PostSortOption postSortOption, String keyword, Pageable pageable);

    //게시글 단건 조회
    UserPostResponseDto getPostById(Long postNo);

    // 게시글 생성
    PostResponseDto createPost(BoardType boardType, PostDto postDto, Long userNo);

    // 게시글 수정
    PostResponseDto updatePost(Long postNo, PostDto postDto);

    // 게시글 삭제
    void deletePost(Long postNo) ;

    // 내가 쓴 게시글 전체 조회
    Page<UserPostResponseDto> getUserPosts(BoardType boardType, Pageable pageable, Long userNo);

    // 조회수
    void viewPost(CustomUserDetails details, Long postNo, HttpServletRequest request);

}
