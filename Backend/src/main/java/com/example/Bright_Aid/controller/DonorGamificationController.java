package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.DonorGamificationDto;
import com.example.Bright_Aid.service.DonorGamificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/donor-gamification")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DonorGamificationController {

    private final DonorGamificationService donorGamificationService;

    @PostMapping
    public ResponseEntity<DonorGamificationDto> createGamification(@Valid @RequestBody DonorGamificationDto dto) {
        try {
            DonorGamificationDto created = donorGamificationService.createGamification(dto);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DonorGamificationDto> getGamificationById(@PathVariable Integer id) {
        try {
            DonorGamificationDto gamification = donorGamificationService.getGamificationById(id);
            return new ResponseEntity<>(gamification, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/donor/{donorId}")
    public ResponseEntity<DonorGamificationDto> getGamificationByDonorId(@PathVariable Integer donorId) {
        try {
            DonorGamificationDto gamification = donorGamificationService.getGamificationByDonorId(donorId);
            return new ResponseEntity<>(gamification, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<DonorGamificationDto>> getAllGamifications() {
        List<DonorGamificationDto> gamifications = donorGamificationService.getAllGamifications();
        return new ResponseEntity<>(gamifications, HttpStatus.OK);
    }

    @GetMapping("/level/{level}")
    public ResponseEntity<List<DonorGamificationDto>> getGamificationsByLevel(@PathVariable String level) {
        List<DonorGamificationDto> gamifications = donorGamificationService.getGamificationsByLevel(level);
        return new ResponseEntity<>(gamifications, HttpStatus.OK);
    }

    @GetMapping("/min-points/{points}")
    public ResponseEntity<List<DonorGamificationDto>> getGamificationsByMinPoints(@PathVariable Integer points) {
        List<DonorGamificationDto> gamifications = donorGamificationService.getGamificationsByMinPoints(points);
        return new ResponseEntity<>(gamifications, HttpStatus.OK);
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<DonorGamificationDto>> getLeaderboard() {
        List<DonorGamificationDto> leaderboard = donorGamificationService.getLeaderboard();
        return new ResponseEntity<>(leaderboard, HttpStatus.OK);
    }

    @GetMapping("/top/{topN}")
    public ResponseEntity<List<DonorGamificationDto>> getTopRankedDonors(@PathVariable Integer topN) {
        List<DonorGamificationDto> topDonors = donorGamificationService.getTopRankedDonors(topN);
        return new ResponseEntity<>(topDonors, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DonorGamificationDto> updateGamification(
            @PathVariable Integer id,
            @Valid @RequestBody DonorGamificationDto dto) {
        try {
            DonorGamificationDto updated = donorGamificationService.updateGamification(id, dto);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/add-points/{donorId}")
    public ResponseEntity<DonorGamificationDto> addPoints(
            @PathVariable Integer donorId,
            @RequestParam Integer points) {
        try {
            DonorGamificationDto updated = donorGamificationService.addPoints(donorId, points);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGamification(@PathVariable Integer id) {
        try {
            donorGamificationService.deleteGamification(id);
            return new ResponseEntity<>("Gamification deleted successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Gamification not found", HttpStatus.NOT_FOUND);
        }
    }
}