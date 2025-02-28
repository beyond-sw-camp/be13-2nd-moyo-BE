package com.beyond.backend.data.dto.postDto;

import com.beyond.backend.data.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

    private Long postNo;
    private String title;
    private String content;
    private String userName;
    private String createdAt;
    private String updatedAt;


    @Builder
    public PostResponseDto(Post post) {
        this.postNo = post.getNo();
        this.title = post.getPostTitle();
        this.content = post.getPostContent();
        this.userName = post.getUser().getUsername();
    }


}