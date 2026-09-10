package com.task.breem.dtos.responses;

import com.task.breem.data.models.Enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private UUID id;
    private UUID userId;
    private NotificationType type;
    private String title;
    private String body;
    private boolean isRead;
    private LocalDateTime createdAt;
}
