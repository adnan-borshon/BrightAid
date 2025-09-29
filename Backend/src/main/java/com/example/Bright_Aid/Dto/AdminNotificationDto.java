package com.example.Bright_Aid.Dto;

import com.example.Bright_Aid.Entity.AdminNotification;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminNotificationDto {

    private Integer notificationId;

    @NotNull(message = "Admin ID is required")
    private Integer adminId;

    @NotNull(message = "Notification type is required")
    private AdminNotification.NotificationType notificationType;

    private String title;

    private String message;

    @Builder.Default
    private AdminNotification.Priority priority = AdminNotification.Priority.MEDIUM;

    @Builder.Default
    private Boolean isRead = false;

    private String actionUrl;

    private LocalDateTime readAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}