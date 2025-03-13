package com.beyond.backend.domain.project.service;

import com.beyond.backend.domain.project.dto.ScheduleRequestDto;
import com.beyond.backend.domain.project.dto.ScheduleResponseDto;
import com.beyond.backend.domain.project.entity.Schedule;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface ScheduleService {

    ScheduleResponseDto createSchedule(Long userNo, ScheduleRequestDto dto);

    ScheduleResponseDto updateSchedule(Long scheduleId, Long userNo, ScheduleRequestDto dto);

    void deleteSchedule(Long scheduleId, Long userNo);

    ScheduleResponseDto getSchedule(Long scheduleId, Long userNo);

    List<ScheduleResponseDto> getSchedulesByProject(Long scheduleId, Long userNo);

    void sendAlert();

}
