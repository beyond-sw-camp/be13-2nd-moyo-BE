package com.beyond.backend.domain.like.repository;

import com.beyond.backend.domain.comment.dto.CommentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * <p>
 *
 * <p>packageName    : com.beyond.backend.domain.like.repository
 * <p>fileName       : LikeRepositoryCustom
 * <p>author         : hyunjo
 * <p>date           : 25. 3. 6.
 * <p>description    :
 */
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 3. 6.        hyunjo             최초 생성
 */public interface LikeRepositoryCustom {
    // 유저가 좋아요한 댓글 전체 조회
    Page<CommentResponseDto> getUserLikedComments(Long userNo, Pageable pageable);
}
