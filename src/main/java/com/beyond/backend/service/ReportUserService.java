package com.beyond.backend.service;

import com.beyond.backend.data.dto.ReportUserDto.ReportUserAdminResDto;
import com.beyond.backend.data.dto.ReportUserDto.ReportUserDto;
import com.beyond.backend.data.dto.ReportUserDto.ReportUserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * <p>
 * <p>packageName    : com.beyond.backend.service
 * <p>fileName       : ReportService
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

public interface ReportUserService {
    ReportUserResponseDto createReport(ReportUserDto reportDto);

    ReportUserResponseDto addComment(ReportUserAdminResDto reportUserAdminResDto);

    Page<ReportUserResponseDto> getReportList(Long no, Pageable pageable);
}
