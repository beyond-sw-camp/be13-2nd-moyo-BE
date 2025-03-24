package com.beyond.backend.domain.message.service;

import com.beyond.backend.domain.message.dto.MessageDto;
import com.beyond.backend.domain.message.dto.MessageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * <p>
 * <p>packageName    : com.beyond.backend.service
 * <p>fileName       : MessageService
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

public interface MessageService {

    MessageResponseDto getMessage(Long messageNo); // 단일 조회

    MessageResponseDto messageWrite(MessageDto messageDto);

    Page<MessageResponseDto> getMessageList(String type, Pageable pageable);

    Object deleteMessage(Long messageNo);

    long getUnreadMessageCount();

    // 3,4,5,6 얘네 나중에 시큐리티? 되면 User 객체로..
}
