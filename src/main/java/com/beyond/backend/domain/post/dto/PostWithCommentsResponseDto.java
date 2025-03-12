package com.beyond.backend.domain.post.dto;

import com.beyond.backend.domain.comment.dto.CommentResponseDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *
 * <p>packageName    : com.beyond.backend.domain.post.dto
 * <p>fileName       : PostWithCommentsResponseDto
 * <p>author         : hyunjo
 * <p>date           : 25. 3. 11.
 * <p>description    :
 */
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 3. 11.        hyunjo             최초 생성
 */

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PostWithCommentsResponseDto {

 //게시글 단 건 조회( 게시글 상세 조회 ) 용 dto
 private Long postNo;
    private String title;
    private String content;
    private Long userNo;
    private String userName;
    private int viewCount;
    private int bookmarkCount;
    private int commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 댓글 목록 추가
    private List<CommentResponseDto> comments;

    public PostWithCommentsResponseDto(PostResponseDto post, List<CommentResponseDto> comments) {
        this.postNo = post.getPostNo();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.userNo = post.getUserNo();
        this.userName = post.getUserName();
        this.viewCount = post.getViewCount();
        this.bookmarkCount = post.getBookmarkCount();
        this.commentCount = post.getCommentCount();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.comments = comments;
    }
}