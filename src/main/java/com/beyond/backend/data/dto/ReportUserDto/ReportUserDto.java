package com.beyond.backend.data.dto.ReportUserDto;

import com.beyond.backend.data.entity.ReportUserType;
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
public class ReportUserDto {

    private Long reporterNo;
    private Long reportedNo;

    private ReportUserType reportUserType;
    private String content;
}
