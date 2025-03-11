package com.beyond.backend.domain.comment.service;

import com.beyond.backend.domain.comment.dto.CommentDto;
import com.beyond.backend.domain.comment.dto.CommentResponseDto;
import com.beyond.backend.domain.post.dto.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * <p>
 *
 * <p>packageName    : com.beyond.backend.service
 * <p>fileName       : CommentService
 * <p>author         : hyunjo
 * <p>date           : 25. 3. 4.
 * <p>description    :
 */
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 3. 4.        hyunjo             최초 생성
 */public interface CommentService {

     // 댓글 생성 (자유게시판에서만 )
    CommentResponseDto createComment(CommentDto commentDto, Long userNo);

    // 댓글 수정
    CommentResponseDto updateComment(Long commentNo, CommentDto commentDto, Long userNo);

    // 댓글 삭제
    void deleteComment(Long commentNo, Long userNo);

    // 유저가 쓴 댓글 전체 조회
    Page<CommentResponseDto> getUserComments(Long userNo, Pageable pageable);

    // 게시글 하나의 댓글 전체 조회
    Page<CommentResponseDto> getPostComments(Long postNo, Pageable pageable);

    // 내가 댓글 단 게시글 전체 조회
    Page<PostResponseDto> getUserCommentPosts(Long userNo, Pageable pageable);

    // 댓글 좋아요
    String checkCommentLike(Long commentNo, Long userNo);

    // 유저가 좋아요한 댓글 전체 조회
    Page<CommentResponseDto> getUserLikedComments(Long userNo, Pageable pageable);
}
