package com.beyond.backend.data.dto.postDto;

import com.beyond.backend.data.entity.BoardType;
import com.beyond.backend.data.entity.Post;
import com.beyond.backend.data.entity.PostStatus;
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
 * <p>packageName    : com.beyond.backend.data.dto.postDto
 * <p>fileName       : UserPostDto
 * <p>author         : hyunjo
 * <p>date           : 25. 3. 5.
 * <p>description    :
 */
/*
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 3. 5.        hyunjo             최초 생성
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserPostResponseDto { // 회원 개인 페이지에서 게시글 조회
    // 따로 분리한 이유는 회원 개인 페이지에서 게시글 조회의 경우 boardType 필요, 게시글 상태도 비활성인 경우도 가져옴

    // 북마크한 게시글 전체 조회

        private Long postNo;
        private String title;
        private String content;
        private Long userNo; // 유저 넘버로 게시글 조회하기 위해 돌려줌
        private String userName;
        private BoardType boardType;
        private PostStatus postStatus;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;




        public UserPostResponseDto(Post post) {
            this.postNo = post.getNo();
            this.title = post.getPostTitle();
            this.content = post.getPostContent();
            this.userNo = post.getUser().getNo();
            this.userName = post.getUser().getUsername();
            this.boardType = post.getBoardType();
            this.postStatus = post.getPostStatus();
            this.createdAt = post.getCreatedAt();
            this.updatedAt = post.getUpdatedAt();
        }

    }