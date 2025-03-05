package com.beyond.backend.service;

import com.beyond.backend.data.dto.commentDto.CommentDto;
import com.beyond.backend.data.dto.commentDto.CommentResponseDto;
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
    CommentResponseDto createComment(CommentDto commentDto);

    // 댓글 수정
    CommentResponseDto updateComment(Long commentNo, CommentDto commentDto);

    // 댓글 삭제
    void deleteComment(Long commentNo, Long userNo);

    // 유저가 쓴 댓글 전체 조회
    Page<CommentResponseDto> getUserComments(Long userNo, Pageable pageable);
}
