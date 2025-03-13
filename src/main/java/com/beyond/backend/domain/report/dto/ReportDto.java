package com.beyond.backend.domain.report.dto;

import com.beyond.backend.domain.report.entity.ReportType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * <p>packageName    : com.beyond.backend.controller
 * <p>fileName       : ReportDto
 * <p>author         : mlnstone
 * <p>date           : 2025. 3. 3.
 * <p>description    :
 */
/*
===========================================================
DATE              AUTHOR             NOTE
-----------------------------------------------------------
2025. 3. 3.        mlnstone             최초 생성
*/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {

    private String reportId;

    @NotNull(message="신고 타입을 지정해주십시오.")
    private ReportType reportType;

    @NotBlank(message="신고 내용은 필수 입력 항목입니다.")
    private String content;
    @NotNull(message="신고할 게시글 url이 없습니다.")
    private String url;
}
