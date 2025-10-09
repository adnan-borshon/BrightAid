package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.FundUtilizationDto;
import com.example.Bright_Aid.service.FundUtilizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fund-utilization")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FundUtilizationController {

    private final FundUtilizationService fundUtilizationService;

    @GetMapping("/donor/{donorId}")
    public ResponseEntity<List<FundUtilizationDto>> getFundUtilizationByDonor(@PathVariable Integer donorId) {
        List<FundUtilizationDto> utilizations = fundUtilizationService.getFundUtilizationByDonor(donorId);
        return ResponseEntity.ok(utilizations);
    }

    @GetMapping("/ngo/{ngoId}")
    public ResponseEntity<List<FundUtilizationDto>> getFundUtilizationByNgo(@PathVariable Integer ngoId) {
        List<FundUtilizationDto> utilizations = fundUtilizationService.getFundUtilizationByNgo(ngoId);
        return ResponseEntity.ok(utilizations);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<FundUtilizationDto>> getFundUtilizationByProject(@PathVariable Integer projectId) {
        List<FundUtilizationDto> utilizations = fundUtilizationService.getFundUtilizationByProject(projectId);
        return ResponseEntity.ok(utilizations);
    }

    @GetMapping
    public ResponseEntity<List<FundUtilizationDto>> getAllFundUtilizations() {
        List<FundUtilizationDto> utilizations = fundUtilizationService.getAllFundUtilizations();
        return ResponseEntity.ok(utilizations);
    }
}