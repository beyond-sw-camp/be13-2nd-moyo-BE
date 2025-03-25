package com.beyond.backend.domain.message.dto;

import com.beyond.backend.domain.message.entity.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    private Long no;
    private String content;

    private Long senderNo;
    private String senderUsername; //username
    private String receiverUsername;

    private boolean deletedBySender;
    private boolean deletedByReceiver;
    private boolean isRead;

    private LocalDateTime sendAt;

    public MessageResponseDto(String content) { //삭제시 쓰기 위해.
        this.content = content;
    }

    public static MessageResponseDto returnMessageDto(Message message) {
        String senderUsername = (message.getSender() != null) ? message.getSender().getUsername() : "탈퇴한 회원입니다";
        String receiverUsername = (message.getReceiver() != null) ? message.getReceiver().getUsername() : "탈퇴한 회원입니다";
        Long senderNo = (message.getSender() != null) ? message.getSender().getNo() : -1L; // 👈 여기

        return new MessageResponseDto(
                message.getNo(),
                message.getContent(),
                senderNo,
                senderUsername,
                receiverUsername,
                message.isDeletedBySender(),
                message.isDeletedByReceiver(),
                message.isRead(),
                message.getCreatedAt()
        );
    }

}
