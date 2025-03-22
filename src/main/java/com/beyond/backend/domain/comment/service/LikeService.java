package com.beyond.backend.domain.comment.service;

import com.beyond.backend.domain.comment.dto.CommentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LikeService {

    String toggleCommentLike(Long commentNo);

    Long getLikeCount(Long commentNo);

}
