package com.beyond.backend.domain.post.dto;

import com.beyond.backend.domain.post.entity.Post;
import com.beyond.backend.domain.post.entity.PostStatus;
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
 * <p>packageName    : com.beyond.backend.data.dto
 * <p>fileName       : PostResponseDto
 * <p>author         : hyunjo
 * <p>date           : 25. 2. 2.
 * <p>description    :
 */
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 2. 2.        hyunjo             최초 생성
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    // 게시글 내용 반환 ( 게시글 단건 조회에서 사용 )


    // 댓글 개수 , 좋아요 개수, 북마크 개수

    private Long postNo;
    private String title;
    private String content;
    private Long userNo; // 유저 넘버로 게시글 조회하기 위해 돌려줌
    private String userName;
    private int viewCount;
    private int bookmarkCount;
    private int commentCount;
    private PostStatus postStatus; // 유저가 비뢀성 상태인 경우도 게시글이 보임
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // 댓글 개수도 돌려줘야 함

    // 게시글 돌려줄 때 댓글
/*    private int commentCount;
   */
    // 북마크 개수





    public PostResponseDto(Post post) {
        this.postNo = post.getNo();
        this.title = post.getPostTitle();
        this.content = post.getPostContent();
        this.userNo = post.getUser().getNo();
        this.userName = post.getUser().getUsername();
        this.viewCount = post.getViewCount();
        this.bookmarkCount = post.getBookmarkCount();
        this.commentCount = post.getCommentCount();
        this.postStatus = post.getPostStatus();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();

    }

}