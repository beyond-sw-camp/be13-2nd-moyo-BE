package com.beyond.backend.controller;

import com.beyond.backend.domain.common.entity.Notification;
import com.beyond.backend.domain.common.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Tag(name = "00 알림 API", description = "알림 API")
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * 서버 전송 이벤트(SSE)를 통한 알림 구독
     */
    @Operation(summary = "알림 구독", description = "서버 전송 이벤트(SSE)를 통한 알림 구독")
    @GetMapping("/subscribe")
    public SseEmitter subscribe(String username) {
        return notificationService.createEmitter(username);
    }

    /**
     * 수신된 알림 리스트 조회 (읽지 않은 알림들)
     */
    @Operation(summary = "알림 조회", description = "수신된 알림 리스트 조회 (읽지 않은 알림들)")
    @GetMapping
    public ResponseEntity<List<Notification>> getNotifications(String username) {
        List<Notification> notifications = notificationService.getNotifications(username);
        return ResponseEntity.ok(notifications);
    }

    /**
     * 특정 알림을 읽음 처리
     */
    @Operation(summary = "알림 읽음 처리", description = "특정 알림을 읽음 처리")
    @PostMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(
            String username,
            @PathVariable String notificationId) {
        notificationService.markNotificationAsRead(username, notificationId);
        return ResponseEntity.noContent().build();
    }
}