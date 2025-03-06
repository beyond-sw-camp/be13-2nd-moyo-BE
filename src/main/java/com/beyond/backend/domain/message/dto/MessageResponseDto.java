package com.beyond.backend.domain.message.dto;

import com.beyond.backend.domain.message.entity.Message;
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

    private String content;

    private String senderName;
    private String senderId;
    private String receiverName;
    private String receiverId;

    private boolean deletedBySender;
    private boolean deletedByReceiver;
    private boolean isRead;

    private LocalDateTime sendAt;

    public static MessageResponseDto returnMessageDto(Message message) {

        return new MessageResponseDto(
                message.getContent(),
                null,
                message.getSender().getUsername(),
                null,
                message.getReceiver().getUsername(),
                message.isDeletedBySender(),
                message.isDeletedByReceiver(),
                message.isRead(),
                message.getCreatedAt()
        );
    }

}





