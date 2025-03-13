package com.beyond.backend.controller;

import com.beyond.backend.domain.project.entity.ProjectSortOption;
import com.beyond.backend.domain.team.dto.ScheduleRequestDto;
import com.beyond.backend.domain.team.dto.ScheduleResponseDto;
import com.beyond.backend.domain.team.entity.ScheduleSortOption;
import com.beyond.backend.domain.team.service.ScheduleService;
import com.beyond.backend.domain.user.dto.CustomUserDetails;
import com.beyond.backend.domain.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final AuthService authService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
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

    @GetMapping("/team/{teamNo}")
    public ResponseEntity<Page<ScheduleResponseDto>> getSchedulesByTeam( @PathVariable Long teamNo,
                                                                        @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                        @PageableDefault(size = 10, page = 0 ) Pageable pageable ,
                                                                        @RequestParam(required = false) ScheduleSortOption scheduleSortOption) {
        Long userNo = userDetails.getUser().getNo();

        Page<ScheduleResponseDto> schedulesByTeam = scheduleService.getSchedulesByTeam(teamNo, userNo, pageable,
            scheduleSortOption);

        return ResponseEntity.ok(schedulesByTeam);
    }
}