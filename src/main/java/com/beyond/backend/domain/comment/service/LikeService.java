package com.beyond.backend.domain.comment.service;

import com.beyond.backend.domain.comment.dto.CommentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LikeService {

    String toggleCommentLike(Long commentNo);

    Long getLikeCount(Long commentNo);


    // 유저가 좋아요한 댓글 전체 조회
    Page<CommentResponseDto> getUserLikedComments(Long userNo, Pageable pageable);
}
