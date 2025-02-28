package com.beyond.backend.service.impl;

import com.beyond.backend.data.dto.postDto.PostResponseDto;
import com.beyond.backend.data.entity.BoardType;
import com.beyond.backend.data.entity.Post;
import com.beyond.backend.data.entity.SearchOption;
import com.beyond.backend.data.repository.PostRepository;
import com.beyond.backend.data.repository.PostRepositoryCustom;
import com.beyond.backend.service.PostService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *
 * <p>packageName    : com.beyond.backend.service.impl
 * <p>fileName       : PostServiceImpl
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
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostRepositoryCustom postRepositoryCustom;

    @Override
    public Page<PostResponseDto> getPosts(BoardType boardType, Pageable pageable) {
        return postRepositoryCustom.getPostsByBoardType(boardType, pageable);
    }

    @Override
    public Page<PostResponseDto> searchPosts(BoardType boardType, SearchOption option, String keyword, Pageable pageable) {
        return postRepositoryCustom.searchPosts(boardType, option, keyword, pageable);
    }

    @Override
    public PostResponseDto getPostById(Long postId) {
        Post foundPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));
        return new PostResponseDto(foundPost);
    }
}
