package com.beyond.backend.domain.comment.repository;

import com.beyond.backend.domain.comment.dto.CommentResponseDto;
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

     Page<CommentResponseDto> getUserComments(Long userNo, Pageable pageable);
}
