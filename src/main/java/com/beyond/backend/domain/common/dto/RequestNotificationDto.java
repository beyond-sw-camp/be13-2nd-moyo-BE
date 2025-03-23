package com.beyond.backend.domain.common.dto;

import com.beyond.backend.domain.common.entity.Notification;
import com.beyond.backend.domain.common.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestNotificationDto {

    private String senderId;    // 보낸 사람 ID
    private String receiverId;  // 받는 사람 ID
    private NotificationType notificationType;
    private String message;     // 알림 메시지

    public RequestNotificationDto(Notification notification) {
        this.senderId = notification.getSenderId();
        this.receiverId = notification.getReceiverId();
        this.notificationType = notification.getType();
        this.message = notification.getMessage();
    }
}