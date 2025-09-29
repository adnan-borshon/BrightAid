package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.DonorDto;
import com.example.Bright_Aid.service.DonorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/donors")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DonorController {

    private final DonorService donorService;

    @PostMapping
    public ResponseEntity<DonorDto> saveDonor(@Valid @RequestBody DonorDto donorDto) {
        DonorDto savedDonor = donorService.saveDonor(donorDto);
        return new ResponseEntity<>(savedDonor, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DonorDto>> getAllDonors() {
        List<DonorDto> donors = donorService.getAllDonors();
        return ResponseEntity.ok(donors);
    }

    @GetMapping("/{donorId}")
    public ResponseEntity<DonorDto> getDonorById(@PathVariable Integer donorId) {
        DonorDto donor = donorService.getDonorById(donorId);
        return ResponseEntity.ok(donor);
    }

    @DeleteMapping("/{donorId}")
    public ResponseEntity<Void> deleteDonor(@PathVariable Integer donorId) {
        donorService.deleteDonor(donorId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{donorId}/gamifications")
    public ResponseEntity<List<Integer>> getGamificationIds(@PathVariable Integer donorId) {
        List<Integer> gamificationIds = donorService.getGamificationIds(donorId);
        return ResponseEntity.ok(gamificationIds);
    }

    @PutMapping("/{donorId}/update-stats")
    public ResponseEntity<DonorDto> updateDonationStats(@PathVariable Integer donorId,
                                                        @RequestParam(required = false) BigDecimal additionalAmount,
                                                        @RequestParam(required = false) Integer additionalSchools,
                                                        @RequestParam(required = false) Integer additionalStudents) {
        DonorDto donor = donorService.updateDonationStats(donorId, additionalAmount, additionalSchools, additionalStudents);
        return ResponseEntity.ok(donor);
    }

    @PutMapping("/{donorId}/toggle-anonymous")
    public ResponseEntity<DonorDto> toggleAnonymousStatus(@PathVariable Integer donorId) {
        DonorDto donor = donorService.toggleAnonymousStatus(donorId);
        return ResponseEntity.ok(donor);
    }
}