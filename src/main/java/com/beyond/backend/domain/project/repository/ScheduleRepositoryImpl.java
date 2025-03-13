package com.beyond.backend.domain.project.repository;

import com.beyond.backend.domain.project.dto.AlertResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.beyond.backend.domain.project.entity.QProject.project;
import static com.beyond.backend.domain.project.entity.QSchedule.schedule;
import static com.beyond.backend.domain.team.entity.QTeam.team;
import static com.beyond.backend.domain.teamUser.entity.QTeamUser.teamUser;
import static com.beyond.backend.domain.user.entity.QUser.user;

@Repository
public class ScheduleRepositoryImpl implements ScheduleRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    public ScheduleRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<AlertResponseDto> findSchedulesEndingWithin24Hours() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlus24 = now.plusHours(24);

        return queryFactory
                .select(Projections.constructor(AlertResponseDto.class,
                        user.no,
                        schedule.no))
                .from(schedule)
                .where(schedule.endDate.between(now, nowPlus24),
                        schedule.isAlertSent.isFalse())
                .join(schedule.project, project)
                .join(project.team, team)
                .join(team.teamUsers, teamUser)
                .join(teamUser.user, user)
                .fetch();
    }
}