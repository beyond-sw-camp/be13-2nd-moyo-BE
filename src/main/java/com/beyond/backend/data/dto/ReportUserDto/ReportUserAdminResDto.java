package com.beyond.backend.data.dto.ReportUserDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * <p>packageName    : com.beyond.backend.data.dto.userReportDto
 * <p>fileName       : UserReportAdminDto
 * <p>author         : mlnstone
 * <p>date           : 2025. 3. 3.
 * <p>description    : comment를 달기 위한 dto 입니다.
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
public class ReportUserAdminResDto extends ReportUserDto {
    private Long no; // 신고 no
    private String comment;
}
