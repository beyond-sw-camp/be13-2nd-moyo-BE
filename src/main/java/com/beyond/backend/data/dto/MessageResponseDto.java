package com.beyond.backend.data.dto;

import com.beyond.backend.data.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <p>
 * <p>packageName    : com.beyond.backend.data.dto
 * <p>fileName       : MessageResponseDto
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

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDto {

    private Long messageNo;
    private String content;

    private String senderName;
    private Long senderNo;
    private String senderId;
    private String receiverName;
    private Long receiverNo;
    private String receiverId;

    private LocalDateTime sendAt;

    public static MessageResponseDto returnMessageDto(Message message) {

        return new MessageResponseDto(
                message.getNo(),
                message.getContent(),
                message.getSender().getName(),
                message.getSender().getNo(),
                message.getSender().getUsername(),
                message.getReceiver().getName(),
                message.getReceiver().getNo(),
                message.getReceiver().getUsername(),
                message.getSentAt());
    }
}





