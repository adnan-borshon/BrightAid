package com.example.Bright_Aid.Dto;

import com.example.Bright_Aid.Entity.AdminActivity;
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
public class AdminActivityDto {

    private Integer activityId;

    @NotNull(message = "Admin ID is required")
    private Integer adminId;

    @NotNull(message = "Activity type is required")
    private AdminActivity.ActivityType activityType;

    @NotNull(message = "Target entity is required")
    private AdminActivity.TargetEntity targetEntity;

    @NotNull(message = "Target ID is required")
    private Integer targetId;

    private String activityDescription;

    private String beforeData;

    private String afterData;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}