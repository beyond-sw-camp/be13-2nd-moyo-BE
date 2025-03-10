package com.beyond.backend.controller;

import com.beyond.backend.domain.report.dto.ReportAdminResDto;
import com.beyond.backend.domain.report.dto.ReportDto;
import com.beyond.backend.domain.report.dto.ReportResponseDto;
import com.beyond.backend.domain.report.service.ReportService;
import com.beyond.backend.domain.user.entity.User;
import com.beyond.backend.domain.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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

@RestController
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    private final UserRepository userRepository;

    @Operation(summary = "유저 신고 리스트")
    // role(ADMIN) 추가 예정!
    @GetMapping("/user-reports/{userNo}")
    public ResponseEntity<Page<ReportResponseDto>> getUserReports(
            @PathVariable Long userNo,
            @PageableDefault(size = 10, page = 0, sort = "no") Pageable pageable) {

        User user = getUserByNo(userNo);
        Page<ReportResponseDto> reportResponseDto = reportService.getReportList(user.getNo(), pageable);

        return ResponseEntity.ok(reportResponseDto);
    }


    @Operation(summary = "신고 작성", description = "신고를 생성합니다<br> USER_REPORT(유저)<br> POST_REPORT(게시글)<br> MESSAGE_REPORT(쪽지)<br> OTHER(기타)")
    @PostMapping("/user-reports")
    public ResponseEntity<ReportResponseDto> createReport(
            @Parameter(name = "reporterNo", description = "신고하는사람 no") @RequestParam Long reporterNo,
            @RequestBody ReportDto reportDto) {
        ReportResponseDto reportResponseDto = reportService.createReport(reporterNo, reportDto);

        return ResponseEntity.ok(reportResponseDto);
    }

    @Operation(summary = "comment 작성", description = "어드민이 comment를 작성합니다")
    @PutMapping("/user-reports/{reportNo}")
    // role(ADMIN) 추가 예정!
    public ResponseEntity<ReportResponseDto> updateReport(
            @PathVariable Long reportNo,
            @Parameter(description = "no는 신고번호입니다") @RequestBody ReportAdminResDto reportAdminResDto) {
        ReportResponseDto reportResponseDto = reportService.addComment(reportNo, reportAdminResDto);

        return ResponseEntity.ok(reportResponseDto);

    }


    /**
     * 유저 있는지 메서드
     */
    private User getUserByNo(Long userNo) {
        return userRepository.findById(userNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));
    }
}










