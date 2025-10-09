package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    // Upload school image
    @PostMapping("/school/{schoolId}")
    public ResponseEntity<Map<String, String>> uploadSchoolImage(
            @PathVariable Integer schoolId,
            @RequestParam("image") MultipartFile file) {
        try {
            String imagePath = imageService.saveSchoolImage(schoolId, file);
            return ResponseEntity.ok(Map.of("imagePath", imagePath));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to upload image: " + e.getMessage()));
        }
    }

    // Upload student image
    @PostMapping("/student/{studentId}")
    public ResponseEntity<Map<String, String>> uploadStudentImage(
            @PathVariable Integer studentId,
            @RequestParam("image") MultipartFile file) {
        try {
            String imagePath = imageService.saveStudentImage(studentId, file);
            return ResponseEntity.ok(Map.of("imagePath", imagePath));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to upload image: " + e.getMessage()));
        }
    }

    // Upload donor image
    @PostMapping("/donor/{donorId}")
    public ResponseEntity<Map<String, String>> uploadDonorImage(
            @PathVariable Integer donorId,
            @RequestParam("image") MultipartFile file) {
        try {
            String imagePath = imageService.saveDonorImage(donorId, file);
            return ResponseEntity.ok(Map.of("imagePath", imagePath));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to upload image: " + e.getMessage()));
        }
    }

    // Upload user profile image (for user profiles)
    @PostMapping("/user/{userId}")
    public ResponseEntity<Map<String, String>> uploadUserImage(
            @PathVariable Integer userId,
            @RequestParam("image") MultipartFile file) {
        try {
            String imagePath = imageService.saveUserImage(userId, file);
            return ResponseEntity.ok(Map.of("imagePath", imagePath));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to upload image: " + e.getMessage()));
        }
    }
}