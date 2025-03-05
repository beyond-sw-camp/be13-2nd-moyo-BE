package com.beyond.backend.data.dto.postDto;

import com.beyond.backend.data.entity.BoardType;
import com.beyond.backend.data.entity.Post;
import com.beyond.backend.data.entity.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
    // 게시글 내용 반환 ( 게시글 단건 조회 )
    // 게시글 전체 조회 조회에서도 사용

    private Long postNo;
    private String title;
    private String content;
    private Long userNo; // 유저 넘버로 게시글 조회하기 위해 돌려줌
    private String userName;
    private PostStatus postStatus; // 유저가 비뢀성 상태인 경우도 게시글이 보임
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;




    public PostResponseDto(Post post) {
        this.postNo = post.getNo();
        this.title = post.getPostTitle();
        this.content = post.getPostContent();
        this.userNo = post.getUser().getNo();
        this.userName = post.getUser().getUsername();
        this.postStatus = post.getPostStatus();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }

}