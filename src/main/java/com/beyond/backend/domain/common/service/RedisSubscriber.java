package com.beyond.backend.domain.common.service;

import com.beyond.backend.domain.common.entity.Notification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSubscriber {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final Map<String, List<SseEmitter>> emitters = new ConcurrentHashMap<>();
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);

    /**
     * Redis Pub/Sub 메시지를 처리하는 메서드
     * @param channel    수신된 Redis 채널 이름
     * @param message    수신된 메시지 (Redis Key 값)
     */
    public void onMessage(String channel, String message) {
        log.info("Received message from channel: [{}] with key: {}", channel, message);
        processMessage(message);
    }

    /**
     * Redis에서 알림을 조회하고 클라이언트에게 전달하는 메서드
     * @param key Redis에 저장된 알림 키
     */
    private void processMessage(String key) {
        executorService.submit(() -> {
            try {
                String notificationJson = (String) redisTemplate.opsForValue().get(key);
                if (notificationJson == null) {
                    log.warn("No notification found in Redis for key: {}", key);
                    return;
                }

                Notification notification = objectMapper.readValue(notificationJson, Notification.class);
                sendNotificationToEmitters(notification.getReceiverId(), notification);
            } catch (JsonProcessingException e) {
                log.error("Error parsing notification JSON for key: {}", key, e);
            } catch (Exception e) {
                log.error("Unexpected error while processing notification for key: {}", key, e);
            }
        });
    }

    /**
     * SSE를 통해 알림을 클라이언트로 전송하는 메서드
     * @param notification   전송할 알림 객체
     */
    private void sendNotificationToEmitters(String username, Notification notification) {
        List<SseEmitter> userEmitters = emitters.get(username);
        if (userEmitters != null) {
            List<SseEmitter> deadEmitters = new ArrayList<>();
            for (SseEmitter emitter : userEmitters) {
                try {
                    emitter.send(SseEmitter.event()
                            .name("notification")
                            .data(notification));
                    log.info("Sent SSE notification to user: {} - {}", username, notification);
                } catch (IOException e) {
                    log.error("Failed to send SSE notification to user: {}", username, e);
                    deadEmitters.add(emitter);
                }
            }
            userEmitters.removeAll(deadEmitters);
        }
    }

    /**
     * 클라이언트가 SSE 알림을 구독할 때 Emitter 등록
     * @param emitter SSE Emitter 객체
     */
    public void addEmitter(String username, SseEmitter emitter) {
        emitters.computeIfAbsent(username, k -> new ArrayList<>()).add(emitter);
        log.info("Added SSE Emitter for user: {}", username);
    }

    /**
     * SSE 연결이 종료될 때 Emitter 제거
     * @param emitter 제거할 SSE Emitter 객체
     */
    public void removeEmitter(String username, SseEmitter emitter) {
        emitters.computeIfPresent(username, (key, list) -> {
            list.remove(emitter);
            return list.isEmpty() ? null : list;
        });
        log.info("Removed SSE Emitter for user: {}", username);
    }
}
