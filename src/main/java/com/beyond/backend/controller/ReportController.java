package com.beyond.backend.controller;

import com.beyond.backend.domain.report.dto.ReportAdminResDto;
import com.beyond.backend.domain.report.dto.ReportDto;
import com.beyond.backend.domain.report.dto.ReportResponseDto;
import com.beyond.backend.domain.report.service.ReportService;
import com.beyond.backend.domain.user.dto.CustomUserDetails;
import com.beyond.backend.domain.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * <p>packageName    : com.beyond.backend.controller
 * <p>fileName       : ReportContoller
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

@Tag(name = "10 신고 API", description = "신고 API")
@RestController
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    private final AuthService authService;

    @Operation(summary = "신고 단건 조회")
    @GetMapping("/reports/{reportNo}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReportResponseDto> getReport(@PathVariable Long reportNo) {
        ReportResponseDto reportResponseDto = reportService.getReport(reportNo);
        return ResponseEntity.status(HttpStatus.OK).body(reportResponseDto);
    }

    @Operation(summary = "유저 받은 신고 전체 조회")
    @PreAuthorize("hasRole('ADMIN')") // role(ADMIN) 추가 예정!
    @GetMapping("/reports/user/{userId}")
    public ResponseEntity<Page<ReportResponseDto>> getUserReports(
            @PathVariable String userId,
            @PageableDefault(size = 10, page = 0, sort = "no") Pageable pageable) {
        Page<ReportResponseDto> reportResponseDto = reportService.getUserReportedList(userId, pageable);

        return ResponseEntity.ok(reportResponseDto);
    }

    @Operation(summary = "신고 전체 조회")
    @PreAuthorize("hasRole('ADMIN')") // role(ADMIN) 추가  예정!
    @GetMapping("/reports")
    public ResponseEntity<Page<ReportResponseDto>> getAllReports(
            @PageableDefault(size = 10, page = 0, sort = "no") Pageable pageable) {

        Page<ReportResponseDto> reportResponseDto = reportService.getAllReports(pageable);
        return ResponseEntity.ok(reportResponseDto);
    }

    @Operation(summary = "신고 작성", description = "신고를 생성합니다<br> USER_REPORT(유저)<br> POST_REPORT(게시글)<br> MESSAGE_REPORT(쪽지)<br> OTHER(기타)")
    @PostMapping("/reports")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReportResponseDto> createReport(
            @Valid @RequestBody ReportDto reportDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        ReportResponseDto reportResponseDto = reportService.createReport(userDetails.getNo(), reportDto);
        return ResponseEntity.ok(reportResponseDto);
    }

    @Operation(summary = "신고 처리", description =
            "어드민이 신고를 처리합니다 <br>" +
                    " PENDING,      // 처리 중<br>" +
                    "    ONLY_BANNED,   // 사용자 밴 (게시글 유지)<br>" +
                    "    BANNED       // 사용자 밴 + 모든 작성한 글(게시글+댓글) 삭제")
    @PutMapping("/reports/{reportNo}")
    public ResponseEntity<ReportResponseDto> updateReport(
            @PathVariable Long reportNo,
            @Parameter(description = "no는 신고번호입니다")
            @RequestBody ReportAdminResDto reportAdminResDto) {
        ReportResponseDto reportResponseDto = reportService.processReport(reportNo, reportAdminResDto);
        return ResponseEntity.ok(reportResponseDto);
    }
}