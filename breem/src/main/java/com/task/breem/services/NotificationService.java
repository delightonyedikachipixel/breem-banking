package com.task.breem.services;

import com.task.breem.data.models.Enums.NotificationType;
import com.task.breem.dtos.responses.NotificationResponse;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    NotificationResponse notify(UUID userId, NotificationType type, String title, String body);
    List<NotificationResponse> getForUser(UUID userId);
    List<NotificationResponse> getUnreadForUser(UUID userId);
    NotificationResponse markAsRead(UUID notificationId);
    long unreadCount(UUID userId);
}
