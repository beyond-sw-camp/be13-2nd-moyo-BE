package com.beyond.backend.domain.common.repository;

import com.beyond.backend.domain.common.entity.Notification;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class NotificationRepositoryCustomImpl implements NotificationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public NotificationRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Notification> findByRecipientIdAndRead(Long no, boolean read, Pageable pageable) {
        /*List<Notification> notifications = queryFactory
                .selectFrom()*/
        return null;
    }
}