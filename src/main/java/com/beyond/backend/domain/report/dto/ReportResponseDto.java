package com.beyond.backend.domain.report.dto;

import com.beyond.backend.domain.report.entity.Report;
import com.beyond.backend.domain.report.entity.ReportStatus;
import com.beyond.backend.domain.report.entity.ReportType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <p>
 * <p>packageName    : com.beyond.backend.controller
 * <p>fileName       : ReportResponseDto
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
public class ReportResponseDto {
    private Long no; // 신고 번호

    private String reporterId;
    private String reportedId;

    private ReportType reportType;
    private ReportStatus reportStatus;
    private String url;
    private String content;

    private String comment;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reportTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime commentTime;

    public static ReportResponseDto reportFrom(Report report) {
        String reporterId = (report.getReporter() != null) ? report.getReporter().getUsername() : "탈퇴한 회원입니다";
        String receiverId = (report.getReported() != null) ? report.getReported().getUsername() : "탈퇴한 회원입니다";

        return new ReportResponseDto(
                report.getNo(),
                reporterId,
                receiverId,
                report.getReportType(),
                report.getReportStatus(),
                report.getUrl(),
                report.getContent(),
                report.getComment(),
                report.getCreatedAt(),
                report.getUpdatedAt()
        );
    }
}
