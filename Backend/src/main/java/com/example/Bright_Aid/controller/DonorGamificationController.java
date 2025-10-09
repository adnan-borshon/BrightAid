package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.DonorGamificationDto;
import com.example.Bright_Aid.service.DonorGamificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/donor-gamifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DonorGamificationController {

    private final DonorGamificationService donorGamificationService;

    @GetMapping
    public ResponseEntity<List<DonorGamificationDto>> getAllDonorGamification() {
        List<DonorGamificationDto> gamificationList = donorGamificationService.getAllDonorGamification();
        return ResponseEntity.ok(gamificationList);
    }

    @GetMapping("/donor/{donorId}")
    public ResponseEntity<DonorGamificationDto> getDonorGamificationByDonorId(@PathVariable Integer donorId) {
        DonorGamificationDto gamification = donorGamificationService.getDonorGamificationByDonorId(donorId);
        return ResponseEntity.ok(gamification);
    }

    @GetMapping("/donor/{donorId}/unique-schools")
    public ResponseEntity<Integer> getUniqueSchoolsCountByDonor(@PathVariable Integer donorId) {
        Integer count = donorGamificationService.getUniqueSchoolsCountByDonor(donorId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/donor/{donorId}/stats")
    public ResponseEntity<Map<String, Object>> getDonorStats(@PathVariable Integer donorId) {
        Map<String, Object> stats = donorGamificationService.getDonorStats(donorId);
        return ResponseEntity.ok(stats);
    }

    @PostMapping
    public ResponseEntity<DonorGamificationDto> createDonorGamification(@RequestBody DonorGamificationDto donorGamificationDto) {
        DonorGamificationDto created = donorGamificationService.createOrUpdateDonorGamification(donorGamificationDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/donor/{donorId}")
    public ResponseEntity<DonorGamificationDto> updateDonorGamification(@PathVariable Integer donorId, @RequestBody DonorGamificationDto donorGamificationDto) {
        donorGamificationDto.setDonorId(donorId);
        DonorGamificationDto updated = donorGamificationService.createOrUpdateDonorGamification(donorGamificationDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/donor/{donorId}")
    public ResponseEntity<Void> deleteDonorGamification(@PathVariable Integer donorId) {
        donorGamificationService.deleteDonorGamification(donorId);
        return ResponseEntity.noContent().build();
    }
}