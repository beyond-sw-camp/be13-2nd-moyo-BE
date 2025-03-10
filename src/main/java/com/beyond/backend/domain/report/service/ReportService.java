package com.beyond.backend.domain.report.service;

import com.beyond.backend.domain.report.dto.ReportAdminResDto;
import com.beyond.backend.domain.report.dto.ReportDto;
import com.beyond.backend.domain.report.dto.ReportResponseDto;
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

public interface ReportService {
    ReportResponseDto createReport(Long userNo, ReportDto reportDto);

    ReportResponseDto addComment(Long reportNo, ReportAdminResDto reportAdminResDto);

    Page<ReportResponseDto> getReportList(Long no, Pageable pageable);
}
