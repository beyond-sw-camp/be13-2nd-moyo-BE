package com.beyond.backend.domain.common.repository;

import com.beyond.backend.domain.common.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationRepositoryCustom {

    /**
     * 특정 사용자(recipientId)의 읽음 상태에 따른 알림을 페이징 처리하여 조회합니다.
     * @param read 읽음 여부
     * @param pageable 페이징 정보
     * @return 해당 조건에 맞는 알림 목록 Page 객체
     */
    Page<Notification> findByRecipientIdAndRead(Long no, boolean read, Pageable pageable);
}
