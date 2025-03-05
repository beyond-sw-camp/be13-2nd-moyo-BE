package com.beyond.backend.data.dto.commentDto;

import com.beyond.backend.data.entity.BoardType;
import com.beyond.backend.data.entity.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *
 * <p>packageName    : com.beyond.backend.data.dto
 * <p>fileName       : CommentDto
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
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentDto {

    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    private Long userNo;

    private Long postNo;
}
