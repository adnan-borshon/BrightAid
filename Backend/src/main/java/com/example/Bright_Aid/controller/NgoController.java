package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.NgoDto;
import com.example.Bright_Aid.Entity.Ngo;
import com.example.Bright_Aid.service.NgoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ngos")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class NgoController {

    private final NgoService ngoService;

    @PostMapping
    public ResponseEntity<NgoDto> createNgo(@Valid @RequestBody NgoDto ngoDto) {
        log.info("REST request to create NGO with name: {}", ngoDto.getNgoName());

        NgoDto createdNgo = ngoService.createNgo(ngoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNgo);
    }

    @GetMapping("/{ngoId}")
    public ResponseEntity<NgoDto> getNgoById(@PathVariable Integer ngoId) {
        log.info("REST request to get NGO with ID: {}", ngoId);

        NgoDto ngo = ngoService.getNgoById(ngoId);
        return ResponseEntity.ok(ngo);
    }

    @GetMapping
    public ResponseEntity<List<NgoDto>> getAllNgos() {
        log.info("REST request to get all NGOs");

        List<NgoDto> ngos = ngoService.getAllNgos();
        return ResponseEntity.ok(ngos);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<NgoDto>> getAllNgos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ngoId") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        log.info("REST request to get NGOs with pagination - page: {}, size: {}, sortBy: {}, sortDirection: {}",
                page, size, sortBy, sortDirection);

        Sort sort = sortDirection.equalsIgnoreCase("DESC") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<NgoDto> ngos = ngoService.getAllNgos(pageable);

        return ResponseEntity.ok(ngos);
    }

    @GetMapping("/by-status/{status}")
    public ResponseEntity<List<NgoDto>> getNgosByVerificationStatus(
            @PathVariable Ngo.VerificationStatus status) {
        log.info("REST request to get NGOs with verification status: {}", status);

        List<NgoDto> ngos = ngoService.getNgosByVerificationStatus(status);
        return ResponseEntity.ok(ngos);
    }

    @GetMapping("/verified")
    public ResponseEntity<List<NgoDto>> getVerifiedNgos() {
        log.info("REST request to get all verified NGOs");

        List<NgoDto> ngos = ngoService.getVerifiedNgos();
        return ResponseEntity.ok(ngos);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<NgoDto>> getPendingNgos() {
        log.info("REST request to get all pending NGOs");

        List<NgoDto> ngos = ngoService.getPendingNgos();
        return ResponseEntity.ok(ngos);
    }

    @GetMapping("/by-registration/{registrationNumber}")
    public ResponseEntity<NgoDto> getNgoByRegistrationNumber(@PathVariable String registrationNumber) {
        log.info("REST request to get NGO with registration number: {}", registrationNumber);

        NgoDto ngo = ngoService.getNgoByRegistrationNumber(registrationNumber);
        return ResponseEntity.ok(ngo);
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<NgoDto> getNgoByUserId(@PathVariable Integer userId) {
        log.info("REST request to get NGO for user ID: {}", userId);

        NgoDto ngo = ngoService.getNgoByUserId(userId);
        return ResponseEntity.ok(ngo);
    }

    @PutMapping("/{ngoId}")
    public ResponseEntity<NgoDto> updateNgo(
            @PathVariable Integer ngoId,
            @Valid @RequestBody NgoDto ngoDto) {
        log.info("REST request to update NGO with ID: {}", ngoId);

        NgoDto updatedNgo = ngoService.updateNgo(ngoId, ngoDto);
        return ResponseEntity.ok(updatedNgo);
    }

    @PatchMapping("/{ngoId}/verify")
    public ResponseEntity<NgoDto> verifyNgo(
            @PathVariable Integer ngoId,
            @RequestParam Integer adminId) {
        log.info("REST request to verify NGO ID: {} by admin ID: {}", ngoId, adminId);

        NgoDto verifiedNgo = ngoService.verifyNgo(ngoId, adminId);
        return ResponseEntity.ok(verifiedNgo);
    }

    @PatchMapping("/{ngoId}/reject")
    public ResponseEntity<NgoDto> rejectNgo(
            @PathVariable Integer ngoId,
            @RequestParam Integer adminId) {
        log.info("REST request to reject NGO ID: {} by admin ID: {}", ngoId, adminId);

        NgoDto rejectedNgo = ngoService.rejectNgo(ngoId, adminId);
        return ResponseEntity.ok(rejectedNgo);
    }

    @GetMapping("/{ngoId}/stats")
    public ResponseEntity<?> getNgoStats(@PathVariable Integer ngoId) {
        log.info("REST request to get stats for NGO ID: {}", ngoId);
        
        var stats = ngoService.getNgoStats(ngoId);
        return ResponseEntity.ok(stats);
    }

    @DeleteMapping("/{ngoId}")
    public ResponseEntity<Void> deleteNgo(@PathVariable Integer ngoId) {
        log.info("REST request to delete NGO with ID: {}", ngoId);

        ngoService.deleteNgo(ngoId);
        return ResponseEntity.noContent().build();
    }
}