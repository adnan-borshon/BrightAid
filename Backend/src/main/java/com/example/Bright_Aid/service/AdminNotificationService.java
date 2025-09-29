package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Entity.Admin;
import com.example.Bright_Aid.Entity.AdminNotification;
import com.example.Bright_Aid.Dto.AdminNotificationDto;
import com.example.Bright_Aid.repository.AdminNotificationRepository;
import com.example.Bright_Aid.repository.AdminRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminNotificationService {

    private final AdminNotificationRepository adminNotificationRepository;
    private final AdminRepository adminRepository;

    public AdminNotificationService(AdminNotificationRepository adminNotificationRepository, AdminRepository adminRepository) {
        this.adminNotificationRepository = adminNotificationRepository;
        this.adminRepository = adminRepository;
    }

    // Create or update AdminNotification
    public AdminNotificationDto saveAdminNotification(AdminNotificationDto adminNotificationDto) {
        Admin admin = adminRepository.findById(adminNotificationDto.getAdminId())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        AdminNotification adminNotification = AdminNotification.builder()
                .notificationId(adminNotificationDto.getNotificationId())
                .admin(admin)
                .notificationType(adminNotificationDto.getNotificationType())
                .title(adminNotificationDto.getTitle())
                .message(adminNotificationDto.getMessage())
                .priority(adminNotificationDto.getPriority() != null ?
                        adminNotificationDto.getPriority() : AdminNotification.Priority.MEDIUM)
                .isRead(adminNotificationDto.getIsRead() != null ?
                        adminNotificationDto.getIsRead() : false)
                .actionUrl(adminNotificationDto.getActionUrl())
                .readAt(adminNotificationDto.getReadAt())
                .build();

        AdminNotification saved = adminNotificationRepository.save(adminNotification);
        return mapToDto(saved);
    }

    // Get all admin notifications
    public List<AdminNotificationDto> getAllAdminNotifications() {
        return adminNotificationRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Get admin notification by ID
    public AdminNotificationDto getAdminNotificationById(Integer notificationId) {
        return adminNotificationRepository.findById(notificationId)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Admin notification not found"));
    }

    // Delete admin notification
    public void deleteAdminNotification(Integer notificationId) {
        if (!adminNotificationRepository.existsById(notificationId)) {
            throw new RuntimeException("Admin notification not found");
        }
        adminNotificationRepository.deleteById(notificationId);
    }

    // Mark notification as read
    public AdminNotificationDto markAsRead(Integer notificationId) {
        AdminNotification notification = adminNotificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Admin notification not found"));

        notification.setIsRead(true);
        notification.setReadAt(LocalDateTime.now());

        AdminNotification saved = adminNotificationRepository.save(notification);
        return mapToDto(saved);
    }

    // Mark notification as unread
    public AdminNotificationDto markAsUnread(Integer notificationId) {
        AdminNotification notification = adminNotificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Admin notification not found"));

        notification.setIsRead(false);
        notification.setReadAt(null);

        AdminNotification saved = adminNotificationRepository.save(notification);
        return mapToDto(saved);
    }

    // Map AdminNotification entity to DTO
    private AdminNotificationDto mapToDto(AdminNotification adminNotification) {
        return AdminNotificationDto.builder()
                .notificationId(adminNotification.getNotificationId())
                .adminId(adminNotification.getAdmin().getAdminId())
                .notificationType(adminNotification.getNotificationType())
                .title(adminNotification.getTitle())
                .message(adminNotification.getMessage())
                .priority(adminNotification.getPriority())
                .isRead(adminNotification.getIsRead())
                .actionUrl(adminNotification.getActionUrl())
                .readAt(adminNotification.getReadAt())
                .createdAt(adminNotification.getCreatedAt())
                .updatedAt(adminNotification.getUpdatedAt())
                .build();
    }
}