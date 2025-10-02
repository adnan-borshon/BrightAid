package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.DonorGamificationDto;
import com.example.Bright_Aid.Entity.DonorGamification;
import com.example.Bright_Aid.service.DonorGamificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donor-gamifications")
@RequiredArgsConstructor
public class DonorGamificationController {

    private final DonorGamificationService donorGamificationService;

    // GET all gamifications
    @GetMapping
    public ResponseEntity<List<DonorGamificationDto>> getAllGamifications() {
        return ResponseEntity.ok(donorGamificationService.getAllGamifications());
    }

    // GET by ID
    @GetMapping("/{id}")
    public ResponseEntity<DonorGamificationDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(donorGamificationService.getGamificationById(id));
    }

    // POST new gamification
    @PostMapping
    public ResponseEntity<DonorGamificationDto> create(@RequestBody DonorGamificationDto dto) {
        return ResponseEntity.ok(donorGamificationService.create(dto));
    }

    // PUT update gamification
    @PutMapping("/{id}")
    public ResponseEntity<DonorGamificationDto> update(@PathVariable Integer id,
                                                       @RequestBody DonorGamificationDto dto) {
        return ResponseEntity.ok(donorGamificationService.update(id, dto));
    }

    // DELETE gamification
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        donorGamificationService.deleteGamification(id);
        return ResponseEntity.noContent().build();
    }

    // GET gamification by donor ID
    @GetMapping("/donor/{donorId}")
    public ResponseEntity<DonorGamificationDto> getByDonorId(@PathVariable Integer donorId) {
        return ResponseEntity.ok(donorGamificationService.getGamificationByDonorId(donorId));
    }

    // GET unique schools count for donor
    @GetMapping("/donor/{donorId}/unique-schools")
    public ResponseEntity<Integer> getUniqueSchoolsCount(@PathVariable Integer donorId) {
        return ResponseEntity.ok(donorGamificationService.getUniqueSchoolsCount(donorId));
    }

    // GET donor statistics
    @GetMapping("/donor/{donorId}/stats")
    public ResponseEntity<com.example.Bright_Aid.Dto.DonorStatsDto> getDonorStats(@PathVariable Integer donorId) {
        return ResponseEntity.ok(donorGamificationService.getDonorStats(donorId));
    }

}