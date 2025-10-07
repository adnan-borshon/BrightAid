package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Dto.UserProfileDto;
import com.example.Bright_Aid.Entity.User;
import com.example.Bright_Aid.Entity.UserProfile;
import com.example.Bright_Aid.repository.UserProfileRepository;
import com.example.Bright_Aid.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    @Transactional
    public UserProfileDto createUserProfile(UserProfileDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId()));

        UserProfile userProfile = UserProfile.builder()
                .user(user)
                .fullName(dto.getFullName())
                .phone(dto.getPhone())
                .address(dto.getAddress())

                .status(dto.getStatus())
                .profileImageUrl(dto.getProfileImageUrl())
                .lastLogin(dto.getLastLogin())
                .build();

        UserProfile saved = userProfileRepository.save(userProfile);
        return convertToDto(saved);
    }

    @Transactional(readOnly = true)
    public UserProfileDto getUserProfileById(Integer id) {
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserProfile not found with id: " + id));
        return convertToDto(userProfile);
    }

    // Get user profile by userId - for easier frontend integration
    @Transactional(readOnly = true)
    public UserProfileDto getUserProfileByUserId(Integer userId) {
        UserProfile userProfile = userProfileRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("UserProfile not found for userId: " + userId));
        return convertToDto(userProfile);
    }

    @Transactional(readOnly = true)
    public List<UserProfileDto> getAllUserProfiles() {
        return userProfileRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserProfileDto updateUserProfile(Integer id, UserProfileDto dto) {
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserProfile not found with id: " + id));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId()));

        userProfile.setUser(user);
        userProfile.setFullName(dto.getFullName());
        userProfile.setPhone(dto.getPhone());
        userProfile.setAddress(dto.getAddress());
        userProfile.setStatus(dto.getStatus());
        userProfile.setProfileImageUrl(dto.getProfileImageUrl());
        userProfile.setLastLogin(dto.getLastLogin());

        UserProfile updated = userProfileRepository.save(userProfile);
        return convertToDto(updated);
    }

    @Transactional
    public void deleteUserProfile(Integer id) {
        if (!userProfileRepository.existsById(id)) {
            throw new RuntimeException("UserProfile not found with id: " + id);
        }
        userProfileRepository.deleteById(id);
    }

    private UserProfileDto convertToDto(UserProfile userProfile) {
        return UserProfileDto.builder()
                .profileId(userProfile.getProfileId())
                .userId(userProfile.getUser().getUserId())
                .fullName(userProfile.getFullName())
                .phone(userProfile.getPhone())
                .address(userProfile.getAddress())
                .status(userProfile.getStatus())
                .profileImageUrl(userProfile.getProfileImageUrl())
                .lastLogin(userProfile.getLastLogin())
                .createdAt(userProfile.getCreatedAt())
                .updatedAt(userProfile.getUpdatedAt())
                .build();
    }
}