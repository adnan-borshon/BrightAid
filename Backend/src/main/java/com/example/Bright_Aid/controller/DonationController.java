package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.DonationDto;
import com.example.Bright_Aid.Entity.Donation;
import com.example.Bright_Aid.service.DonationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DonationController {

    private final DonationService donationService;

    @PostMapping
    public ResponseEntity<DonationDto> saveDonation(@Valid @RequestBody DonationDto donationDto) {
        DonationDto savedDonation = donationService.saveDonation(donationDto);
        return new ResponseEntity<>(savedDonation, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DonationDto>> getAllDonations() {
        List<DonationDto> donations = donationService.getAllDonations();
        return ResponseEntity.ok(donations);
    }

    @GetMapping("/{donationId}")
    public ResponseEntity<DonationDto> getDonationById(@PathVariable Integer donationId) {
        DonationDto donation = donationService.getDonationById(donationId);
        return ResponseEntity.ok(donation);
    }

    @DeleteMapping("/{donationId}")
    public ResponseEntity<Void> deleteDonation(@PathVariable Integer donationId) {
        donationService.deleteDonation(donationId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{donationId}/complete-payment")
    public ResponseEntity<DonationDto> completePayment(@PathVariable Integer donationId) {
        DonationDto donation = donationService.completePayment(donationId);
        return ResponseEntity.ok(donation);
    }

    @PutMapping("/{donationId}/fail-payment")
    public ResponseEntity<DonationDto> failPayment(@PathVariable Integer donationId) {
        DonationDto donation = donationService.failPayment(donationId);
        return ResponseEntity.ok(donation);
    }

    @PutMapping("/{donationId}/payment-status")
    public ResponseEntity<DonationDto> updatePaymentStatus(@PathVariable Integer donationId,
                                                           @RequestParam Donation.PaymentStatus paymentStatus) {
        DonationDto donation = donationService.updatePaymentStatus(donationId, paymentStatus);
        return ResponseEntity.ok(donation);
    }
}