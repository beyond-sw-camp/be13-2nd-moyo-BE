package com.beyond.backend.domain.team.service;

import com.beyond.backend.domain.team.dto.ScheduleRequestDto;
import com.beyond.backend.domain.team.dto.ScheduleResponseDto;
import com.beyond.backend.domain.team.entity.ScheduleSortOption;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ScheduleService {

    ScheduleResponseDto createSchedule(ScheduleRequestDto dto);

    ScheduleResponseDto updateSchedule(Long scheduleId, ScheduleRequestDto dto);

    void deleteSchedule(Long scheduleId);

    ScheduleResponseDto getSchedule(Long scheduleId);

    Page<ScheduleResponseDto> getSchedulesByTeam(Long teamNo, Pageable pageable, ScheduleSortOption scheduleSortOption);

    void sendAlert();
}