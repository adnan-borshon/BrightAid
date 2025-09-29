package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.UserProfileDto;
import com.example.Bright_Aid.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-profiles")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PostMapping
    public ResponseEntity<UserProfileDto> createUserProfile(@Valid @RequestBody UserProfileDto dto) {
        UserProfileDto created = userProfileService.createUserProfile(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDto> getUserProfileById(@PathVariable Integer id) {
        UserProfileDto dto = userProfileService.getUserProfileById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<UserProfileDto>> getAllUserProfiles() {
        List<UserProfileDto> userProfiles = userProfileService.getAllUserProfiles();
        return ResponseEntity.ok(userProfiles);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserProfileDto> updateUserProfile(
            @PathVariable Integer id,
            @Valid @RequestBody UserProfileDto dto) {
        UserProfileDto updated = userProfileService.updateUserProfile(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserProfile(@PathVariable Integer id) {
        userProfileService.deleteUserProfile(id);
        return ResponseEntity.noContent().build();
    }
}