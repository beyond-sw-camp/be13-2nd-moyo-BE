package com.beyond.backend.service;

import com.beyond.backend.data.dto.MessageDto;
import com.beyond.backend.data.dto.MessageResponseDto;

import java.util.List;

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

    MessageResponseDto getMessage(Long userNo, Long messageNo); // 단일 조회

    MessageResponseDto messageWrite(MessageDto messageDto);

//    Object deleteMessageBySender(Long id, String userId);
//
//    Object deleteMessageByReceiver(Long id, String userId);

    List<MessageResponseDto> getSentMessagesByOrder(Long userNo);

    List<MessageResponseDto> getSentMessagesByLatest(Long userNo);

    List<MessageResponseDto> getReceivedMessagesByOrder(Long userNo);

    List<MessageResponseDto> getReceivedMessagesByLatest(Long userNo);

    Object deleteMessage(Long userNo, Long messageNo);

    // 3,4,5,6 얘네 나중에 시큐리티? 되면 User 객체로..
}
