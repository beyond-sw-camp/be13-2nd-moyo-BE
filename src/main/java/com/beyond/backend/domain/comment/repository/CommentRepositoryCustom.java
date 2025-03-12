package com.beyond.backend.domain.comment.repository;

import com.beyond.backend.domain.comment.dto.CommentResponseDto;
import com.beyond.backend.domain.comment.entity.CommentSortOption;
import com.beyond.backend.domain.post.dto.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * <p>
 *
 * <p>packageName    : com.beyond.backend.data.repository
 * <p>fileName       : CommentRepositoryCustom
 * <p>author         : hyunjo
 * <p>date           : 25. 3. 5.
 * <p>description    :
 */
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 3. 5.        hyunjo             최초 생성
 */public interface CommentRepositoryCustom {

     // 유저의 댓글 전체 조회
     Page<CommentResponseDto> getUserComments(Long userNo, Pageable pageable);

     // 게시글의 댓글 전체 조회
    Page<CommentResponseDto> getPostComments(Long postNo, CommentSortOption commentSortOption, Pageable pageable);

    // 유저가 댓글을 작성한 게시글 전체 조회
    Page<PostResponseDto> getUserCommentPosts(Long userNo, Pageable pageable);


}
