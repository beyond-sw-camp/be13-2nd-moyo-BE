package com.beyond.backend.domain.post.dto;

import com.beyond.backend.domain.post.entity.BoardType;
import com.beyond.backend.domain.post.entity.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *
 * <p>packageName    : com.beyond.backend.data.dto
 * <p>fileName       : PostDto
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
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostDto {
    @NotBlank(message = "게시글 제목은 필수 입력 항목입니다.")
    private String title;

    @NotBlank(message = "게시글 내용은 필수 입력 항목입니다.")
    private String content;

    @NotNull(message = "게시판 타입을 지정해 주십시오.")
    private BoardType boardType;

    private PostStatus postStatus;
}