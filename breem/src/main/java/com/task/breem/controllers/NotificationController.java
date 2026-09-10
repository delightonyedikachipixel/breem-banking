package com.task.breem.controllers;

import com.task.breem.dtos.responses.ApiResponse;
import com.task.breem.dtos.responses.NotificationResponse;
import com.task.breem.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getForUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(ApiResponse.success("Notifications retrieved", notificationService.getForUser(userId)));
    }

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getUnread(@PathVariable UUID userId) {
        return ResponseEntity.ok(ApiResponse.success("Unread notifications retrieved", notificationService.getUnreadForUser(userId)));
    }

    @GetMapping("/user/{userId}/unread-count")
    public ResponseEntity<ApiResponse<Long>> unreadCount(@PathVariable UUID userId) {
        return ResponseEntity.ok(ApiResponse.success("Unread count retrieved", notificationService.unreadCount(userId)));
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse<NotificationResponse>> markAsRead(@PathVariable UUID notificationId) {
        return ResponseEntity.ok(ApiResponse.success("Notification marked as read", notificationService.markAsRead(notificationId)));
    }
}
