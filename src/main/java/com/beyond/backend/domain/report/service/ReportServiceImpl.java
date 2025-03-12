package com.beyond.backend.domain.report.service;

import com.beyond.backend.domain.report.dto.ReportAdminResDto;
import com.beyond.backend.domain.report.dto.ReportDto;
import com.beyond.backend.domain.report.dto.ReportResponseDto;
import com.beyond.backend.domain.report.entity.Report;
import com.beyond.backend.domain.report.repository.ReportRepository;
import com.beyond.backend.domain.user.dto.CustomUserDetails;
import com.beyond.backend.domain.user.entity.User;
import com.beyond.backend.domain.user.repository.UserRepository;
import com.beyond.backend.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final AuthService authService;

    @Override // role(ADMIN) 추가 예정!
    public Page<ReportResponseDto> getReportList(CustomUserDetails userDetails, String userId, Pageable pageable) {
        User user = userRepository.findByUsername(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다"));
        Page<Report> reported = reportRepository.findAllByReported_No(user.getNo(), pageable);
        if (authService.isAdminFromUserDetails(userDetails))
            return reported.map(ReportResponseDto::from);

        throw new AccessDeniedException("권한이 없습니다.");
    }

    @Override
    @Transactional
    public ReportResponseDto createReport(User reporter, ReportDto reportDto) {
        User reported = userRepository.findByUsername(reportDto.getReportId())
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 유저입니다"));
        if (reported.getNo().equals(reporter.getNo())) {
            throw new IllegalArgumentException("자신에게 신고를 할 수 없습니다.");
        }
        
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
    @Transactional
    public ReportResponseDto addComment(CustomUserDetails userDetails, Long reportNo, ReportAdminResDto reportAdminResDto) {
        Report report = reportRepository.findById(reportNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 신고가 없습니다"));
        if (authService.isAdminFromUserDetails(userDetails)) {

            report.updateComment(reportAdminResDto.getComment()); //생성자
            report.markAsCompleted();

            reportRepository.save(report);
            return ReportResponseDto.from(report);
        }
        throw new AccessDeniedException("권한이 없습니다.");
    }

}
