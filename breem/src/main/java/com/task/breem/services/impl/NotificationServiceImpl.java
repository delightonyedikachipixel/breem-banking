package com.task.breem.services.impl;

import com.task.breem.data.models.Enums.NotificationType;
import com.task.breem.data.models.Notification;
import com.task.breem.data.models.User;
import com.task.breem.data.repository.NotificationRepository;
import com.task.breem.data.repository.UserRepository;
import com.task.breem.dtos.responses.NotificationResponse;
import com.task.breem.exceptions.ResourceNotFoundException;
import com.task.breem.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    public NotificationResponse notify(UUID userId, NotificationType type, String title, String body) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        Notification notification = Notification.builder()
                .user(user)
                .type(type)
                .title(title)
                .body(body)
                .isRead(false)
                .build();

        return toResponse(notificationRepository.save(notification));
    }

    @Override
    public List<NotificationResponse> getForUser(UUID userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<NotificationResponse> getUnreadForUser(UUID userId) {
        return notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public NotificationResponse markAsRead(UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found: " + notificationId));
        notification.setRead(true);
        return toResponse(notificationRepository.save(notification));
    }

    @Override
    public long unreadCount(UUID userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    private NotificationResponse toResponse(Notification n) {
        return NotificationResponse.builder()
                .id(n.getId())
                .userId(n.getUser().getId())
                .type(n.getType())
                .title(n.getTitle())
                .body(n.getBody())
                .isRead(n.isRead())
                .createdAt(n.getCreatedAt())
                .build();
    }
}
