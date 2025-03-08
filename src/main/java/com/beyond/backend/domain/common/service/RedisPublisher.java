package com.beyond.backend.domain.common.service;

import com.beyond.backend.domain.common.entity.Notification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    /**
     * Redis Pub/Sub을 사용하여 알림을 발행하는 메서드
     * @param channel    Redis 채널 이름
     * @param message    발행할 메시지 (알림 키)
     */
    public void publish(String channel, String message) {
        log.info("Publishing message to channel: [{}] at time: {} with key: {}", channel, Instant.now(), message);
        redisTemplate.convertAndSend(channel, message);
    }

    /**
     * Redis에 알림을 저장하고 TTL을 설정하는 메서드
     * @param key        알림을 저장할 Redis 키
     * @param notification 알림 객체
     * @param ttl        TTL (만료 시간)
     * @param timeUnit   TTL의 시간 단위 (DAYS, HOURS, MINUTES 등)
     */
    public void saveNotificationWithTTL(String key, Notification notification, long ttl, TimeUnit timeUnit) {
        try {
            String notificationJson = objectMapper.writeValueAsString(notification);
            redisTemplate.opsForValue().set(key, notificationJson, ttl, timeUnit);
            log.info("✅ [REDIS STORE] Key: {} | Expiration: {} {} | Data: {}",
                    key, ttl, timeUnit, notificationJson);
        } catch (JsonProcessingException e) {
            log.error("Error serializing notification: {}", e.getMessage(), e);
        }
    }
}
