package com.beyond.backend.domain.team.repository;

import com.beyond.backend.domain.project.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleRepositoryCustom{
}
