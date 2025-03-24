package com.beyond.backend.domain.team.repository;

import com.beyond.backend.domain.team.dto.AlertResponseDto;
import com.beyond.backend.domain.team.dto.ScheduleResponseDto;
import com.beyond.backend.domain.team.entity.ScheduleSortOption;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.beyond.backend.domain.team.entity.QSchedule.schedule;
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
                .join(schedule.team, team)
                .join(team.teamUsers, teamUser)
                .join(teamUser.user, user)
                .fetch();
    }


    @Override
    public Page<ScheduleResponseDto> getSchedulesByTeam(Long teamNo, Long userNo, Pageable pageable, ScheduleSortOption scheduleSortOption) {

        List<ScheduleResponseDto> allSchedules = queryFactory
            .select(Projections.constructor(ScheduleResponseDto.class,
                        schedule.no,
                        schedule.title,
                        schedule.startDate,
                        schedule.endDate,
                        schedule.description,
                        schedule.status,
                        schedule.createdBy
            ))
            .from(schedule)
            .join(schedule.team, team)
            .where(team.no.eq(teamNo))
            .orderBy(getOrderSpecifier(scheduleSortOption)) // 최신순, 조회순
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();



        // 전체 프로젝트 개수 조회
        JPAQuery<Long> totalCount = queryFactory
            .select(schedule.count())
            .from(schedule)
            .join(schedule.team, team)
            .where(team.no.eq(teamNo));


        return PageableExecutionUtils.getPage(allSchedules, pageable, totalCount::fetchOne);
    }


    private OrderSpecifier<?> getOrderSpecifier(ScheduleSortOption scheduleSortOption){

        if(scheduleSortOption == null){
            return schedule.createdAt.desc(); // 새로 등록된 순
        }

        switch (scheduleSortOption){
            case START_DATE:
                return schedule.startDate.asc();
            case END_DATE:
                return schedule.endDate.asc();
            // default = 새로 등록된 순
            default:
                return schedule.createdAt.desc();
        }

    }
}
