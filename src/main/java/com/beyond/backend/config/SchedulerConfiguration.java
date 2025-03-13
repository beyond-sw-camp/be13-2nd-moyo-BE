package com.beyond.backend.config;

import com.beyond.backend.domain.team.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfiguration {

    private final ScheduleService scheduleService;

    @Scheduled(fixedRate = 3600000)
    public void notifyOneDayBeforeSchedule() {
        LocalDateTime currentTime = LocalDateTime.now(); //로그찍기용
        scheduleService.sendAlert();
        log.info("{} 에 알림감", currentTime.toString());
    }
}