package com.beyond.backend.domain.team.service;

import com.beyond.backend.domain.project.dto.ScheduleRequestDto;
import com.beyond.backend.domain.project.dto.ScheduleResponseDto;

import java.util.List;

public interface ScheduleService {

    ScheduleResponseDto createSchedule(Long userNo, ScheduleRequestDto dto);

    ScheduleResponseDto updateSchedule(Long scheduleId, Long userNo, ScheduleRequestDto dto);

    void deleteSchedule(Long scheduleId, Long userNo);

    ScheduleResponseDto getSchedule(Long scheduleId, Long userNo);

    List<ScheduleResponseDto> getSchedulesByTeam(Long scheduleId, Long userNo);

    void sendAlert();

}
