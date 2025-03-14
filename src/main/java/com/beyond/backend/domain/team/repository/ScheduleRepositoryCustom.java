package com.beyond.backend.domain.team.repository;

import com.beyond.backend.domain.team.dto.AlertResponseDto;
import com.beyond.backend.domain.team.dto.ScheduleResponseDto;
import com.beyond.backend.domain.team.entity.ScheduleSortOption;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ScheduleRepositoryCustom {

    List<AlertResponseDto> findSchedulesEndingWithin24Hours();

    Page<ScheduleResponseDto> getSchedulesByTeam(Long teamNo, Long userNo, Pageable pageable, ScheduleSortOption scheduleSortOption );
}
