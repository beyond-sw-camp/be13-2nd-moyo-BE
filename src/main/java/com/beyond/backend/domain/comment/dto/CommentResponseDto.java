package com.beyond.backend.domain.comment.dto;

import com.beyond.backend.domain.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * <p>packageName    : com.beyond.backend.data.dto.commentDto
 * <p>fileName       : CommentRequestDto
 * <p>author         : hyunjo
 * <p>date           : 25. 3. 4.
 * <p>description    :
 */
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 3. 4.        hyunjo             최초 생성
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {


    private Long commentNo;
    private String content;
    private Long userNo; // userNo를 돌려줘서 나중에 로그인한 회원 비교로 사용할 예정
    private String userName;
    private Long postNo;
    private int likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 뎃글 개수와 좋아요 수 전달
/*    private int commentCount;
    private int likeCount;*/

    public CommentResponseDto(Comment comment) {
        this.commentNo = comment.getNo();
        this.content = comment.getContent();
        this.userNo = comment.getUser().getNo();
        this.userName = comment.getUser().getUsername();
        this.postNo = comment.getPost().getNo();
        this.likeCount = comment.getLikeCount();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }
}
