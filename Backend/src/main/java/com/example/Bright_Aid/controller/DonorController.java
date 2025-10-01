package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.DonorDto;
import com.example.Bright_Aid.service.DonorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/donors")
@RequiredArgsConstructor
public class DonorController {

    private final DonorService donorService;

    @PostMapping
    public DonorDto createDonor(@RequestBody DonorDto donorDto) {
        return donorService.createDonor(donorDto);
    }

    @GetMapping("/user/{userId}")
    public Optional<DonorDto> getByUserId(@PathVariable Integer userId) {
        return donorService.getByUserId(userId);
    }

    @GetMapping("/anonymous")
    public List<DonorDto> getAnonymousDonors() {
        return donorService.getAnonymousDonors();
    }

    @GetMapping("/min-donation/{amount}")
    public List<DonorDto> getByMinDonation(@PathVariable BigDecimal amount) {
        return donorService.getDonorsByMinDonation(amount);
    }

    @GetMapping("/top/{limit}")
    public List<DonorDto> getTopDonors(@PathVariable int limit) {
        return donorService.getTopDonors(limit);
    }

    @GetMapping
    public List<DonorDto> getAllDonors() {
        return donorService.getAllDonors();
    }

    @GetMapping("/{donorId}")
    public Optional<DonorDto> getDonorById(@PathVariable Integer donorId) {
        return donorService.getDonorById(donorId);
    }
}
