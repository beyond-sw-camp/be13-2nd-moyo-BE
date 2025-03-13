package com.beyond.backend.domain.project.service;

import com.beyond.backend.domain.common.dto.RequestNotificationDto;
import com.beyond.backend.domain.common.entity.NotificationType;
import com.beyond.backend.domain.common.service.NotificationService;
import com.beyond.backend.domain.project.dto.AlertResponseDto;
import com.beyond.backend.domain.project.dto.ScheduleRequestDto;
import com.beyond.backend.domain.project.dto.ScheduleResponseDto;
import com.beyond.backend.domain.project.entity.Project;
import com.beyond.backend.domain.project.entity.Schedule;
import com.beyond.backend.domain.project.repository.ProjectRepository;
import com.beyond.backend.domain.project.repository.ScheduleRepository;
import com.beyond.backend.domain.teamUser.entity.TeamUser;
import com.beyond.backend.domain.user.entity.User;
import com.beyond.backend.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;


    @Transactional
    public ScheduleResponseDto createSchedule(Long userNo, ScheduleRequestDto dto) {

        validateScheduleRequest(dto);

        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("project not found"));

        Schedule schedule = Schedule.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();

        project.getSchedules().add(schedule);

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
        Project project = schedule.getProject();
        validateUserAccessToProject(userNo, project);

        // 일정 업데이트
        schedule.update(
                dto.getTitle(),
                dto.getDescription(),
                dto.getStartDate(),
                dto.getEndDate()
        );

        return new ScheduleResponseDto(schedule);
    }

    public ScheduleResponseDto getSchedule(Long scheduleId, Long userNo){
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found with id: " + scheduleId));
        validateUserAccessToProject(userNo, schedule.getProject());

        return new ScheduleResponseDto(schedule);
    }

    public List<ScheduleResponseDto> getSchedulesByProject(Long scheduleId, Long userNo){
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found with id: " + scheduleId));
        validateUserAccessToProject(userNo, schedule.getProject());

        return null;
    }


    @Transactional
    public void deleteSchedule(Long scheduleId, Long userNo) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found with id: " + scheduleId));
        validateUserAccessToProject(userNo, schedule.getProject());
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
        }
    }

    /**
     * 스케줄(*) -> (1)프로젝트(1) -> 팀(1) -> (*)팀 유저(*) -> (1)유저
     * 유저1 스케줄1
     * 유저1 스케줄2
     * 유저2 스케줄2
     * 유저3 스케줄1
     */


    private void validateScheduleRequest(ScheduleRequestDto dto) {
        if (dto.getEndDate() != null && dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
    }

    private void validateUserAccessToProject(Long userNo, Project project) {
        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userNo));

        // 프로젝트 팀 멤버인지 확인하는 로직
        boolean isMember = project.getTeam().getTeamUsers().stream()
                .anyMatch(teamUser -> teamUser.getUser().equals(user));

        if (!isMember) {
            throw new IllegalArgumentException("User does not have permission to update this schedule");
        }
    }
}
