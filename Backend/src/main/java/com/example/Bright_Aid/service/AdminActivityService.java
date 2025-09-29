package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Entity.Admin;
import com.example.Bright_Aid.Entity.AdminActivity;
import com.example.Bright_Aid.Dto.AdminActivityDto;
import com.example.Bright_Aid.repository.AdminActivityRepository;
import com.example.Bright_Aid.repository.AdminRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminActivityService {

    private final AdminActivityRepository adminActivityRepository;
    private final AdminRepository adminRepository;

    public AdminActivityService(AdminActivityRepository adminActivityRepository, AdminRepository adminRepository) {
        this.adminActivityRepository = adminActivityRepository;
        this.adminRepository = adminRepository;
    }

    // Create or update AdminActivity
    public AdminActivityDto saveAdminActivity(AdminActivityDto adminActivityDto) {
        Admin admin = adminRepository.findById(adminActivityDto.getAdminId())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        AdminActivity adminActivity = AdminActivity.builder()
                .activityId(adminActivityDto.getActivityId())
                .admin(admin)
                .activityType(adminActivityDto.getActivityType())
                .targetEntity(adminActivityDto.getTargetEntity())
                .targetId(adminActivityDto.getTargetId())
                .activityDescription(adminActivityDto.getActivityDescription())
                .beforeData(adminActivityDto.getBeforeData())
                .afterData(adminActivityDto.getAfterData())
                .build();

        AdminActivity saved = adminActivityRepository.save(adminActivity);
        return mapToDto(saved);
    }

    // Get all admin activities
    public List<AdminActivityDto> getAllAdminActivities() {
        return adminActivityRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Get admin activity by ID
    public AdminActivityDto getAdminActivityById(Integer activityId) {
        return adminActivityRepository.findById(activityId)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Admin activity not found"));
    }

    // Delete admin activity
    public void deleteAdminActivity(Integer activityId) {
        if (!adminActivityRepository.existsById(activityId)) {
            throw new RuntimeException("Admin activity not found");
        }
        adminActivityRepository.deleteById(activityId);
    }

    // Map AdminActivity entity to DTO
    private AdminActivityDto mapToDto(AdminActivity adminActivity) {
        return AdminActivityDto.builder()
                .activityId(adminActivity.getActivityId())
                .adminId(adminActivity.getAdmin().getAdminId())
                .activityType(adminActivity.getActivityType())
                .targetEntity(adminActivity.getTargetEntity())
                .targetId(adminActivity.getTargetId())
                .activityDescription(adminActivity.getActivityDescription())
                .beforeData(adminActivity.getBeforeData())
                .afterData(adminActivity.getAfterData())
                .createdAt(adminActivity.getCreatedAt())
                .updatedAt(adminActivity.getUpdatedAt())
                .build();
    }
}