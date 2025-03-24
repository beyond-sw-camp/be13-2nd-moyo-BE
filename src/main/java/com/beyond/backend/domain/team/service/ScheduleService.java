package com.beyond.backend.domain.team.service;

import com.beyond.backend.domain.team.dto.ScheduleRequestDto;
import com.beyond.backend.domain.team.dto.ScheduleResponseDto;
import com.beyond.backend.domain.team.entity.ScheduleSortOption;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ScheduleService {

    ScheduleResponseDto createSchedule(Long userNo, ScheduleRequestDto dto);

    ScheduleResponseDto updateSchedule(Long userNo, Long scheduleId, ScheduleRequestDto dto);

    void deleteSchedule(Long userNo, Long scheduleId);

    ScheduleResponseDto getSchedule(Long userNo, Long scheduleId);

    Page<ScheduleResponseDto> getSchedulesByTeam(Long userNo, Long teamNo, Pageable pageable, ScheduleSortOption scheduleSortOption);

    void sendAlert();
}