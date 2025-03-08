package com.beyond.backend.domain.common.service;


import com.beyond.backend.domain.common.dto.RequestNotificationDto;
import com.beyond.backend.domain.common.entity.Notification;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

public interface NotificationService {

    void sendNotification(RequestNotificationDto dto);

    SseEmitter createEmitter(String userId);

    void sendHeartbeat(String userId, SseEmitter emitter, ScheduledExecutorService executor);

    void setEmitterCallbacks(String userId, SseEmitter emitter, ScheduledExecutorService executor);


    List<Notification> getNotifications(String userId);


    void markNotificationAsRead(String userId, String notificationId);
}
