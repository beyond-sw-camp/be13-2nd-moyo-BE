package com.beyond.backend.domain.common.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    public void publish(String topic, Object message) {
        log.info("Publishing message to channel: [{}] at time: {} with message: {}", topic, Instant.now(), message);
        redisTemplate.convertAndSend(topic, message);
        log.info("Published message to channel: [{}] at time: {} with message: {}", topic, Instant.now(), message);
    }


}
