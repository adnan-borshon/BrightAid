package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.AdminActivityDto;
import com.example.Bright_Aid.service.AdminActivityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin-activities")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminActivityController {

    private final AdminActivityService adminActivityService;

    @PostMapping
    public ResponseEntity<AdminActivityDto> saveAdminActivity(@Valid @RequestBody AdminActivityDto adminActivityDto) {
        AdminActivityDto savedActivity = adminActivityService.saveAdminActivity(adminActivityDto);
        return new ResponseEntity<>(savedActivity, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AdminActivityDto>> getAllAdminActivities() {
        List<AdminActivityDto> activities = adminActivityService.getAllAdminActivities();
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/{activityId}")
    public ResponseEntity<AdminActivityDto> getAdminActivityById(@PathVariable Integer activityId) {
        AdminActivityDto activity = adminActivityService.getAdminActivityById(activityId);
        return ResponseEntity.ok(activity);
    }

    @DeleteMapping("/{activityId}")
    public ResponseEntity<Void> deleteAdminActivity(@PathVariable Integer activityId) {
        adminActivityService.deleteAdminActivity(activityId);
        return ResponseEntity.noContent().build();
    }
}