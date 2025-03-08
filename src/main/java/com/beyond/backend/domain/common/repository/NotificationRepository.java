package com.beyond.backend.domain.common.repository;

import com.beyond.backend.domain.common.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface NotificationRepository extends JpaRepository<Notification, Long> {



}
