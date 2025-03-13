package com.beyond.backend.domain.project.repository;

import com.beyond.backend.domain.project.dto.AlertResponseDto;

import java.util.List;

public interface ScheduleRepositoryCustom {

    List<AlertResponseDto> findSchedulesEndingWithin24Hours();
}
