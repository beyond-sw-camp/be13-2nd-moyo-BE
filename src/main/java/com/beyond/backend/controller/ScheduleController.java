package com.beyond.backend.controller;

import com.beyond.backend.domain.team.dto.ScheduleRequestDto;
import com.beyond.backend.domain.team.dto.ScheduleResponseDto;
import com.beyond.backend.domain.team.entity.ScheduleSortOption;
import com.beyond.backend.domain.team.service.ScheduleService;
import com.beyond.backend.domain.user.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "07 스케줄 API", description = "스케줄 API")
@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ScheduleResponseDto> createSchedule(@Valid @RequestBody ScheduleRequestDto requestDto ) {
        ScheduleResponseDto responseDto = scheduleService.createSchedule(requestDto);

        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{scheduleNo}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(@PathVariable Long scheduleNo,
                                                              @Valid @RequestBody ScheduleRequestDto dto ) {
        ScheduleResponseDto responseDto = scheduleService.updateSchedule(scheduleNo, dto);

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{scheduleNo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleNo) {

        scheduleService.deleteSchedule(scheduleNo);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{scheduleNo}")
    public ResponseEntity<ScheduleResponseDto> getSchedule(@PathVariable Long scheduleNo) {
        ScheduleResponseDto responseDto = scheduleService.getSchedule(scheduleNo);


        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/team/{teamNo}")
    public ResponseEntity<Page<ScheduleResponseDto>> getSchedulesByTeam(@PathVariable Long teamNo,
                                                                        @PageableDefault(size = 10, page = 0) Pageable pageable,
                                                                        @RequestParam(required = false) ScheduleSortOption scheduleSortOption) {

        Page<ScheduleResponseDto> schedulesByTeam = scheduleService.getSchedulesByTeam(teamNo, pageable, scheduleSortOption);
        return ResponseEntity.ok(schedulesByTeam);
    }
}