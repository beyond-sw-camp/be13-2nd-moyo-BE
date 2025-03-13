package com.beyond.backend.domain.team.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.beyond.backend.domain.project.dto.ScheduleRequestDto;
import com.beyond.backend.domain.project.dto.ScheduleResponseDto;
import com.beyond.backend.domain.team.entity.ScheduleSortOption;

public interface ScheduleService {

    ScheduleResponseDto createSchedule(Long userNo, ScheduleRequestDto dto);

    ScheduleResponseDto updateSchedule(Long scheduleId, Long userNo, ScheduleRequestDto dto);

    void deleteSchedule(Long scheduleId, Long userNo);

    ScheduleResponseDto getSchedule(Long scheduleId, Long userNo);

    Page<ScheduleResponseDto> getSchedulesByTeam(Long scheduleId, Long userNo, Pageable pageable, ScheduleSortOption scheduleSortOption );

    void sendAlert();

}
