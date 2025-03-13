package com.beyond.backend.domain.team.service;

import com.beyond.backend.domain.team.dto.ScheduleRequestDto;
import com.beyond.backend.domain.team.dto.ScheduleResponseDto;
import com.beyond.backend.domain.team.entity.ScheduleSortOption;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ScheduleService {

    ScheduleResponseDto createSchedule(Long userNo, ScheduleRequestDto dto);

    ScheduleResponseDto updateSchedule(Long scheduleId, Long userNo, ScheduleRequestDto dto);

    void deleteSchedule(Long scheduleId, Long userNo);

    ScheduleResponseDto getSchedule(Long scheduleId, Long userNo);

    Page<ScheduleResponseDto> getSchedulesByTeam(Long teamNo, Long userNo, Pageable pageable, ScheduleSortOption scheduleSortOption );

    void sendAlert();

}
