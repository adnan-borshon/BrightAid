package com.example.Bright_Aid.Dto;

import com.example.Bright_Aid.Entity.UserProfile;
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
public class UserProfileDto {

    private Integer profileId;

    @NotNull(message = "User ID is required")
    private Integer userId;

    private String fullName;

    private String phone;

    private String address;


    private UserProfile.UserStatus status;

    private String profileImageUrl;

    private LocalDateTime lastLogin;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}