package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.VerificationRequestDto;
import com.example.Bright_Aid.service.VerificationRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/verification-requests")
@RequiredArgsConstructor
public class VerificationRequestController {

    private final VerificationRequestService verificationRequestService;

    @PostMapping
    public ResponseEntity<VerificationRequestDto> createVerificationRequest(@Valid @RequestBody VerificationRequestDto dto) {
        VerificationRequestDto created = verificationRequestService.createVerificationRequest(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VerificationRequestDto> getVerificationRequestById(@PathVariable Integer id) {
        VerificationRequestDto dto = verificationRequestService.getVerificationRequestById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<VerificationRequestDto>> getAllVerificationRequests() {
        List<VerificationRequestDto> requests = verificationRequestService.getAllVerificationRequests();
        return ResponseEntity.ok(requests);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VerificationRequestDto> updateVerificationRequest(
            @PathVariable Integer id,
            @Valid @RequestBody VerificationRequestDto dto) {
        VerificationRequestDto updated = verificationRequestService.updateVerificationRequest(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVerificationRequest(@PathVariable Integer id) {
        verificationRequestService.deleteVerificationRequest(id);
        return ResponseEntity.noContent().build();
    }
}