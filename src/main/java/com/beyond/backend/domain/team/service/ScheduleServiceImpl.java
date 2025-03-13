package com.beyond.backend.domain.team.service;

import com.beyond.backend.domain.common.dto.RequestNotificationDto;
import com.beyond.backend.domain.common.entity.NotificationType;
import com.beyond.backend.domain.common.service.NotificationService;
import com.beyond.backend.domain.team.dto.AlertResponseDto;
import com.beyond.backend.domain.team.dto.ScheduleRequestDto;
import com.beyond.backend.domain.team.dto.ScheduleResponseDto;
import com.beyond.backend.domain.team.entity.Schedule;
import com.beyond.backend.domain.team.entity.ScheduleSortOption;
import com.beyond.backend.domain.team.entity.ScheduleStatus;
import com.beyond.backend.domain.team.entity.Team;
import com.beyond.backend.domain.team.repository.ScheduleRepository;
import com.beyond.backend.domain.team.repository.TeamRepository;
import com.beyond.backend.domain.user.entity.User;
import com.beyond.backend.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final TeamRepository teamRepository;

    @Transactional
    public ScheduleResponseDto createSchedule(Long userNo, ScheduleRequestDto dto) {

        validateScheduleRequest(dto);

        Team team = teamRepository.findById(dto.getTeamNo())
                .orElseThrow(() -> new IllegalArgumentException("team not found"));

        Schedule schedule = Schedule.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .team(team)
                .status(ScheduleStatus.PENDING)
                .isAlertSent(false)
                .build();

        team.addSchedule(schedule);
        scheduleRepository.save(schedule);

        return new ScheduleResponseDto(schedule);
    }

    @Transactional
    public ScheduleResponseDto updateSchedule(Long scheduleId, Long userNo, ScheduleRequestDto dto) {
        // 유효성 검증
        validateScheduleRequest(dto);

        // 일정 조회
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found with id: " + scheduleId));

        // 프로젝트 접근 권한 확인 (선택적)
        Team team = schedule.getTeam();
        validateUserAccessToTeam(userNo, team);

        // 일정 업데이트
        schedule.update(
                dto.getTitle(),
                dto.getDescription(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getStatus()
        );

        return new ScheduleResponseDto(schedule);
    }

    public ScheduleResponseDto getSchedule(Long scheduleId, Long userNo){
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found with id: " + scheduleId));
        validateUserAccessToTeam(userNo, schedule.getTeam());

        return new ScheduleResponseDto(schedule);
    }

    @Override
    public Page<ScheduleResponseDto> getSchedulesByTeam(Long teamNo, Long userNo, Pageable pageable, ScheduleSortOption scheduleSortOption) {

        // 팀 존재 여부
        Team team = teamRepository.findById(teamNo).orElseThrow(
            () -> new IllegalArgumentException("해당하는 팀이 없습니다.")
        );

        // 회원이 팀에 속하는지
        validateUserAccessToTeam(userNo, team);

        //
        Page<ScheduleResponseDto> schedules = scheduleRepository.getSchedulesByTeam(teamNo, userNo, pageable,
            scheduleSortOption);

        return schedules;
    }


    @Transactional
    public void deleteSchedule(Long scheduleId, Long userNo) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found with id: " + scheduleId));
        validateUserAccessToTeam(userNo, schedule.getTeam());
        scheduleRepository.delete(schedule);
    }

    @Override
    @Transactional
    public void sendAlert() {
        List<AlertResponseDto> result = scheduleRepository.findSchedulesEndingWithin24Hours();
        for (AlertResponseDto s : result) {
            User receiver = userRepository.findById(s.getReceiverNo())
                    .orElseThrow(() -> new IllegalArgumentException("user not found"));

            Schedule schedule = scheduleRepository.findById(s.getScheduleNo())
                    .orElseThrow(() -> new IllegalArgumentException("schedule not found"));

            notificationService.sendNotification(
                    new RequestNotificationDto(
                            "System Alert",
                            receiver.getUsername(),
                            NotificationType.SYSTEM,
                            schedule.getTitle() + "일정 D-1"));
            schedule.updateAlertSent(true);
        }
    }

    private void validateScheduleRequest(ScheduleRequestDto dto) {
        if (dto.getEndDate() != null && dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
    }

    private void validateUserAccessToTeam(Long userNo, Team team) {
        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userNo));

        // 프로젝트 팀 멤버인지 확인하는 로직
        boolean isMember = team.getTeamUsers().stream()
                .anyMatch(teamUser -> teamUser.getUser().equals(user));

        if (!isMember) {
            throw new IllegalArgumentException("User does not have permission to update this schedule");
        }
    }
}
