package com.beyond.backend.domain.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;          // 알림 ID (UUID)

    private String senderId;    // 보낸 사람 ID
    private String receiverId;  // 받는 사람 ID
    private NotificationType type;
    private String message;     // 알림 메시지

    @Builder
    public Notification(String id, String senderId, String receiverId, NotificationType type, String message) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.type = type;
        this.message = message;
    }
}
