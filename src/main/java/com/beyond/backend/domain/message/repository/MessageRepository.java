package com.beyond.backend.domain.message.repository;

import com.beyond.backend.domain.message.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <p>
 * <p>packageName    : com.beyond.backend.data.repository
 * <p>fileName       : MessageRepository
 * <p>author         : mlnstone
 * <p>date           : 2025. 2. 16.
 * <p>description    :
 */
/*
===========================================================
DATE              AUTHOR             NOTE
-----------------------------------------------------------
2025. 2. 16.        mlnstone             최초 생성
*/

public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findAllBySender_NoAndDeletedBySenderFalse(Long userNo, Pageable pageable);

    Page<Message> findAllByReceiver_NoAndDeletedByReceiverFalse(Long userNo, Pageable pageable);

    long countByReceiverNoAndIsReadFalse(Long userNo);
}
