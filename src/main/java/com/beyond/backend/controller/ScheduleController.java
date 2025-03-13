package com.beyond.backend.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.beyond.backend.domain.project.dto.ScheduleRequestDto;
import com.beyond.backend.domain.project.dto.ScheduleResponseDto;
import com.beyond.backend.domain.team.entity.ScheduleSortOption;
import com.beyond.backend.domain.team.service.ScheduleService;
import com.beyond.backend.domain.user.dto.CustomUserDetails;
import com.beyond.backend.domain.user.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid @RequestBody ScheduleRequestDto requestDto
    ) {
        Long userNo = userDetails.getUser().getNo();
        ScheduleResponseDto responseDto = scheduleService.createSchedule(userNo, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
        @PathVariable Long scheduleId,
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid @RequestBody ScheduleRequestDto dto
    ) {
        Long userNo = userDetails.getUser().getNo();
        ScheduleResponseDto responseDto = scheduleService.updateSchedule(scheduleId, userNo, dto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{scheduleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteSchedule(
        @PathVariable Long scheduleId,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userNo = userDetails.getUser().getNo();
        scheduleService.deleteSchedule(scheduleId, userNo);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> getSchedule(
        @PathVariable Long scheduleId,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userNo = userDetails.getUser().getNo();
        ScheduleResponseDto responseDto = scheduleService.getSchedule(scheduleId, userNo);
        return ResponseEntity.ok(responseDto);
    }


    // team 별 스케줄 전체 조회
    @GetMapping("/team/{teamNo}")
    public ResponseEntity<Page<ScheduleResponseDto>> getSchedulesByTeam( @PathVariable Long teamNo,
                                                                            @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                            @PageableDefault(size = 10, page = 0 ) Pageable pageable ,
                                                                            @RequestParam(required = false) ScheduleSortOption scheduleSortOption) {

        Long userNo = userDetails.getUser().getNo();

        Page<ScheduleResponseDto> schedules = scheduleService.getSchedulesByTeam(teamNo, userNo, pageable, scheduleSortOption );

        return ResponseEntity.ok(schedules);
    }
}
















