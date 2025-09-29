package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.FundUtilizationDto;
import com.example.Bright_Aid.Entity.FundUtilization;
import com.example.Bright_Aid.service.FundUtilizationService;
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
@RequestMapping("/api/fund-utilizations")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class FundUtilizationController {

    private final FundUtilizationService fundUtilizationService;

    @PostMapping
    public ResponseEntity<FundUtilizationDto> createFundUtilization(
            @Valid @RequestBody FundUtilizationDto fundUtilizationDto) {
        log.info("REST request to create fund utilization for donation ID: {}", fundUtilizationDto.getDonationId());

        FundUtilizationDto createdUtilization = fundUtilizationService.createFundUtilization(fundUtilizationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUtilization);
    }

    @GetMapping("/{utilizationId}")
    public ResponseEntity<FundUtilizationDto> getFundUtilizationById(@PathVariable Integer utilizationId) {
        log.info("REST request to get fund utilization with ID: {}", utilizationId);

        FundUtilizationDto utilization = fundUtilizationService.getFundUtilizationById(utilizationId);
        return ResponseEntity.ok(utilization);
    }

    @GetMapping
    public ResponseEntity<List<FundUtilizationDto>> getAllFundUtilizations() {
        log.info("REST request to get all fund utilizations");

        List<FundUtilizationDto> utilizations = fundUtilizationService.getAllFundUtilizations();
        return ResponseEntity.ok(utilizations);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<FundUtilizationDto>> getAllFundUtilizations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "utilizationId") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        log.info("REST request to get fund utilizations with pagination - page: {}, size: {}, sortBy: {}, sortDirection: {}",
                page, size, sortBy, sortDirection);

        Sort sort = sortDirection.equalsIgnoreCase("DESC") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<FundUtilizationDto> utilizations = fundUtilizationService.getAllFundUtilizations(pageable);

        return ResponseEntity.ok(utilizations);
    }

    @GetMapping("/by-status/{status}")
    public ResponseEntity<List<FundUtilizationDto>> getFundUtilizationsByStatus(
            @PathVariable FundUtilization.UtilizationStatus status) {
        log.info("REST request to get fund utilizations with status: {}", status);

        List<FundUtilizationDto> utilizations = fundUtilizationService.getFundUtilizationsByStatus(status);
        return ResponseEntity.ok(utilizations);
    }

    @GetMapping("/by-project/{projectId}")
    public ResponseEntity<List<FundUtilizationDto>> getFundUtilizationsByProject(@PathVariable Integer projectId) {
        log.info("REST request to get fund utilizations for project ID: {}", projectId);

        List<FundUtilizationDto> utilizations = fundUtilizationService.getFundUtilizationsByProject(projectId);
        return ResponseEntity.ok(utilizations);
    }

    @GetMapping("/by-donation/{donationId}")
    public ResponseEntity<List<FundUtilizationDto>> getFundUtilizationsByDonation(@PathVariable Integer donationId) {
        log.info("REST request to get fund utilizations for donation ID: {}", donationId);

        List<FundUtilizationDto> utilizations = fundUtilizationService.getFundUtilizationsByDonation(donationId);
        return ResponseEntity.ok(utilizations);
    }

    @PutMapping("/{utilizationId}")
    public ResponseEntity<FundUtilizationDto> updateFundUtilization(
            @PathVariable Integer utilizationId,
            @Valid @RequestBody FundUtilizationDto fundUtilizationDto) {
        log.info("REST request to update fund utilization with ID: {}", utilizationId);

        FundUtilizationDto updatedUtilization = fundUtilizationService.updateFundUtilization(utilizationId, fundUtilizationDto);
        return ResponseEntity.ok(updatedUtilization);
    }

    @PatchMapping("/{utilizationId}/status")
    public ResponseEntity<FundUtilizationDto> updateUtilizationStatus(
            @PathVariable Integer utilizationId,
            @RequestParam FundUtilization.UtilizationStatus status) {
        log.info("REST request to update status for fund utilization ID: {} to status: {}", utilizationId, status);

        FundUtilizationDto updatedUtilization = fundUtilizationService.updateUtilizationStatus(utilizationId, status);
        return ResponseEntity.ok(updatedUtilization);
    }

    @DeleteMapping("/{utilizationId}")
    public ResponseEntity<Void> deleteFundUtilization(@PathVariable Integer utilizationId) {
        log.info("REST request to delete fund utilization with ID: {}", utilizationId);

        fundUtilizationService.deleteFundUtilization(utilizationId);
        return ResponseEntity.noContent().build();
    }
}