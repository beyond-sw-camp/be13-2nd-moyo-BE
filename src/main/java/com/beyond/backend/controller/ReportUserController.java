package com.beyond.backend.controller;

import com.beyond.backend.domain.reportUser.dto.ReportUserAdminResDto;
import com.beyond.backend.domain.reportUser.dto.ReportUserDto;
import com.beyond.backend.domain.reportUser.dto.ReportUserResponseDto;
import com.beyond.backend.domain.reportUser.service.ReportUserService;
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
public class ReportUserController {
    private final ReportUserService reportService;
    private final UserRepository userRepository;

    @Operation(summary = "유저 신고 리스트")
    @GetMapping("/user-reports/{userNo}")
    public ResponseEntity<Page<ReportUserResponseDto>> getUserReports(
            @PathVariable Long userNo,
            @PageableDefault(size = 10, page = 0, sort = "no") Pageable pageable) {

        User user = getUserByNo(userNo);
        Page<ReportUserResponseDto> reportUserResponseDto = reportService.getReportList(user.getNo(), pageable);

        return ResponseEntity.ok(reportUserResponseDto);
    }


    @Operation(summary = "신고 작성", description = "신고를 생성합니다<br> ABUSIVE_LANGUAGE(욕설)<br> SPAMMING(도배)<br> POLITICS(정치)<br> OTHER")
    @PostMapping("/user-reports")
    public ResponseEntity<ReportUserResponseDto> createReport(@RequestBody ReportUserDto reportDto) {
        ReportUserResponseDto reportResponseDto = reportService.createReport(reportDto);

        return ResponseEntity.ok(reportResponseDto);
    }

    @Operation(summary = "comment 작성", description = "어드민이 comment를 작성합니다")
    @PutMapping("/user-reports/comment")
    public ResponseEntity<ReportUserResponseDto> updateReport(@Parameter(description = "no는 신고번호입니다") @RequestBody ReportUserAdminResDto reportUserAdminResDto) {
        ReportUserResponseDto reportResponseDto = reportService.addComment(reportUserAdminResDto);

        return ResponseEntity.ok(reportResponseDto);

    }


    /**
     * 유저 있는지 메서드
     */
    private User getUserByNo(Long userNo) {
        return userRepository.findById(userNo)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}










