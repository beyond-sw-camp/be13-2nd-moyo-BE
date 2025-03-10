package com.beyond.backend.domain.report.service;

import com.beyond.backend.domain.report.dto.ReportAdminResDto;
import com.beyond.backend.domain.report.dto.ReportDto;
import com.beyond.backend.domain.report.dto.ReportResponseDto;
import com.beyond.backend.domain.report.entity.Report;
import com.beyond.backend.domain.report.repository.ReportRepository;
import com.beyond.backend.domain.user.entity.User;
import com.beyond.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * <p>packageName    : com.beyond.backend.service.impl
 * <p>fileName       : ReportServiceImpl
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

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final UserRepository userRepository;
    private final ReportRepository reportRepository;

    @Override // role(ADMIN) 추가 예정!
    public Page<ReportResponseDto> getReportList(Long userNo, Pageable pageable) {
        Page<Report> reported = reportRepository.findAllByReported_No(userNo, pageable);

        return reported.map(ReportResponseDto::from);
    }

    @Override
    @Transactional
    public ReportResponseDto createReport(@RequestParam Long userNo, ReportDto reportDto) {
        User reporter = userRepository.findById(userNo).orElseThrow(() -> new IllegalStateException("잘못된 경로입니다"));
        User reported = userRepository.findById(reportDto.getReportedNo())
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 유저입니다"));
        Report report = Report.builder()
                .reportType(reportDto.getReportType())
                .reporter(reporter)
                .reported(reported)
                .content(reportDto.getContent())
                .url(reportDto.getUrl())
                .build();

        reportRepository.save(report);

        return ReportResponseDto.from(report);
    }

    @Override     // role(ADMIN) 추가 예정!
    public ReportResponseDto addComment(Long reportNo, ReportAdminResDto reportAdminResDto) {
        Report report = reportRepository.findById(reportNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 신고가 없습니다"));

        report.updateComment(reportAdminResDto.getComment());
        report.markAsCompleted();

        reportRepository.save(report);
        return ReportResponseDto.from(report);

    }

}
