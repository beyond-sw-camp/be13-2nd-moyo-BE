package com.beyond.backend.domain.report.service;

import com.beyond.backend.domain.comment.repository.CommentRepository;
import com.beyond.backend.domain.common.exception.ReportException;
import com.beyond.backend.domain.common.exception.UserException;
import com.beyond.backend.domain.common.exception.message.ExceptionMessage;
import com.beyond.backend.domain.post.repository.PostRepository;
import com.beyond.backend.domain.report.dto.ReportAdminResDto;
import com.beyond.backend.domain.report.dto.ReportDto;
import com.beyond.backend.domain.report.dto.ReportResponseDto;
import com.beyond.backend.domain.report.entity.Report;
import com.beyond.backend.domain.report.entity.ReportStatus;
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
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;


    @Override // role(ADMIN) 추가 예정!
    public Page<ReportResponseDto> getUserReportedList(CustomUserDetails userDetails, String userId, Pageable pageable) {
        if (!authService.isAdminFromUserDetails(userDetails))
            throw new AccessDeniedException("권한이 없습니다.");

        User user = userRepository.findByUsername(userId).orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));
        Page<Report> reported = reportRepository.findAllByReported_No(user.getNo(), pageable);

        return reported.map(ReportResponseDto::from);


    }

    @Override
    public Page<ReportResponseDto> getReportList(CustomUserDetails userDetails, Pageable pageable) {
        if (!authService.isAdminFromUserDetails(userDetails)) {
            // 권한이 없는 경우 예외 발생
            System.out.println(userDetails.getUser().getUsername());
            throw new AccessDeniedException("권한이 없습니다.");
        }
        Page<Report> reports = reportRepository.findAll(pageable);
        return reports.map(ReportResponseDto::from);
    }

    @Override
    @Transactional
    public ReportResponseDto createReport(User reporter, ReportDto reportDto) {
        User reported = userRepository.findByUsername(reportDto.getReportId())
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));
        if (reported.getNo().equals(reporter.getNo())) {
            throw new IllegalArgumentException("자신에게 신고를 할 수 없습니다.");
        }

        Report report = Report.builder()
                .reportType(reportDto.getReportType())
                .reportStatus(ReportStatus.PENDING)
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
    public ReportResponseDto processReport(CustomUserDetails userDetails, Long reportNo, ReportAdminResDto reportAdminResDto) {
        Report report = reportRepository.findById(reportNo)
                .orElseThrow(() -> new ReportException(ExceptionMessage.REPORT_NOT_FOUND));
        if (authService.isAdminFromUserDetails(userDetails)) {

            report.updateComment(reportAdminResDto.getComment()); //생성자
            updateReportStatus(report, reportAdminResDto.getStatus());

            reportRepository.save(report);
            return ReportResponseDto.from(report);
        }
        throw new AccessDeniedException("권한이 없습니다.");
    }

    // 신고 처리 로직
    public void updateReportStatus(Report report, ReportStatus status) {
        report.updateReportStatus(status);

        boolean isBanned = (status == ReportStatus.ONLY_BANNED || status == ReportStatus.BANNED);
        boolean isFullyBanned = (status == ReportStatus.BANNED);

        if (isBanned) { // 밴
            report.getReported().updateBan(true);
        }               // 밴+게시글삭제
        if (isFullyBanned) {
            deleteUserComments(report.getReported());
            deleteUserPosts(report.getReported());
        }
    }

    public void deleteUserPosts(User reported) { // ONLY BANNED
        // 해당 사용자가 작성한 모든 게시글 삭제
        postRepository.deleteByUserNo(reported.getNo());
    }

    public void deleteUserComments(User reported) { // BANNED
        // 해당 사용자가 작성한 모든 댓글 삭제
        commentRepository.deleteByUserNo(reported.getNo());
    }
}
