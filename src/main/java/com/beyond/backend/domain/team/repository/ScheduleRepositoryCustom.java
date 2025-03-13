package com.beyond.backend.domain.team.repository;

import com.beyond.backend.domain.team.dto.AlertResponseDto;

import java.util.List;

public interface ScheduleRepositoryCustom {

    List<AlertResponseDto> findSchedulesEndingWithin24Hours();
}
