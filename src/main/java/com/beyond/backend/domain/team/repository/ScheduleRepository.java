package com.beyond.backend.domain.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beyond.backend.domain.project.entity.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleRepositoryCustom {
}
