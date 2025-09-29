package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.AdminNotificationDto;
import com.example.Bright_Aid.service.AdminNotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin-notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminNotificationController {

    private final AdminNotificationService adminNotificationService;

    @PostMapping
    public ResponseEntity<AdminNotificationDto> saveAdminNotification(@Valid @RequestBody AdminNotificationDto adminNotificationDto) {
        AdminNotificationDto savedNotification = adminNotificationService.saveAdminNotification(adminNotificationDto);
        return new ResponseEntity<>(savedNotification, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AdminNotificationDto>> getAllAdminNotifications() {
        List<AdminNotificationDto> notifications = adminNotificationService.getAllAdminNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{notificationId}")
    public ResponseEntity<AdminNotificationDto> getAdminNotificationById(@PathVariable Integer notificationId) {
        AdminNotificationDto notification = adminNotificationService.getAdminNotificationById(notificationId);
        return ResponseEntity.ok(notification);
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteAdminNotification(@PathVariable Integer notificationId) {
        adminNotificationService.deleteAdminNotification(notificationId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{notificationId}/mark-read")
    public ResponseEntity<AdminNotificationDto> markAsRead(@PathVariable Integer notificationId) {
        AdminNotificationDto notification = adminNotificationService.markAsRead(notificationId);
        return ResponseEntity.ok(notification);
    }

    @PutMapping("/{notificationId}/mark-unread")
    public ResponseEntity<AdminNotificationDto> markAsUnread(@PathVariable Integer notificationId) {
        AdminNotificationDto notification = adminNotificationService.markAsUnread(notificationId);
        return ResponseEntity.ok(notification);
    }
}