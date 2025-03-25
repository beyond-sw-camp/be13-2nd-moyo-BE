package com.beyond.backend.domain.common.service;

import com.beyond.backend.domain.common.dto.RequestNotificationDto;
import com.beyond.backend.domain.common.entity.Notification;
import com.beyond.backend.domain.common.entity.NotificationType;
import com.beyond.backend.domain.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final RedisPublisher redisPublisher;
    private final RedisSubscriber redisSubscriber;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 댓글이 저장될 때, 게시글 작성자 혹은 그룹 관리자에게 알림을 발송하는 메서드
     */
    public void sendNotification(RequestNotificationDto dto) {

        Notification notification = Notification.builder()
                .receiverId(dto.getReceiverId())
                .senderId(dto.getSenderId())
                .message(dto.getMessage())
                .type(dto.getNotificationType())
                .build();

        String notificationKey = dto.getReceiverId() + ":" + notification.getId();
        log.info("Notification Key: {}가 저장됨", notificationKey);

        log.info("[SEND NOTIFICATION] {} → {} | Type: {} | Message: {}",
                dto.getSenderId(), dto.getReceiverId(), dto.getNotificationType(), dto.getMessage());

        // Redis에 저장 및 발행
        redisPublisher.saveNotificationWithTTL(notificationKey, notification, 3, TimeUnit.DAYS);
        redisPublisher.publish("notificationChannel", notificationKey);
    }

    /**
     * SSE(서버 전송 이벤트)로 클라이언트가 알림을 구독할 때 사용
     */
    public SseEmitter createEmitter(String username) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        redisSubscriber.addEmitter(username, emitter);

        // 주기적으로 Heartbeat 전송
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> sendHeartbeat(username, emitter, executor), 0, 15, TimeUnit.SECONDS);

        setEmitterCallbacks(username, emitter, executor);
        return emitter;
    }

    /**
     * Heartbeat를 보내서 클라이언트와 연결 유지
     */
    public void sendHeartbeat(String username, SseEmitter emitter, ScheduledExecutorService executor) {
        try {
            emitter.send(SseEmitter.event().name("heartbeat").data("heartbeat"));
        } catch (IOException e) {
            redisSubscriber.removeEmitter(username, emitter);
            executor.shutdown();
        }
    }

    /**
     * SSE 연결이 종료될 경우 Emitter 정리
     */
    public void setEmitterCallbacks(String username, SseEmitter emitter, ScheduledExecutorService executor) {
        emitter.onCompletion(() -> {
            redisSubscriber.removeEmitter(username, emitter);
            executor.shutdown();
        });
        emitter.onTimeout(() -> {
            redisSubscriber.removeEmitter(username, emitter);
            executor.shutdown();
        });
        emitter.onError((Throwable t) -> {
            redisSubscriber.removeEmitter(username, emitter);
            executor.shutdown();
        });
    }

    /**
     * 사용자의 읽지 않은 알림 리스트 조회
     */
    public List<Notification> getNotifications(String username) {
        Set<String> keys = redisTemplate.keys(username + ":*");
        if (keys == null || keys.isEmpty()) {
            return Collections.emptyList();
        }

        List<Notification> notifications = new ArrayList<>();
        for (String key : keys) {
            String notificationJson = (String) redisTemplate.opsForValue().get(key);
            if (notificationJson != null) {
                try {
                    notifications.add(new ObjectMapper().readValue(notificationJson, Notification.class));
                } catch (Exception e) {
                    log.error("Failed to parse notification JSON for key: {}", key, e);
                }
            }
        }
        return notifications;
    }

    /**
     * 특정 알림을 읽음 처리 (Redis에서 삭제)
     */
    public void markNotificationAsRead(String userId, String notificationId) {
        String key = userId + ":" + notificationId;
        log.info("key: {}가 삭제됨", key);
        redisTemplate.delete(key);
    }
}

