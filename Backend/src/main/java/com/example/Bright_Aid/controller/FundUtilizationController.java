package com.example.Bright_Aid.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/fund-utilizations")
@CrossOrigin(origins = "*")
public class FundUtilizationController {

    @PostMapping
    public ResponseEntity<String> createFundUtilization(@RequestBody Map<String, Object> utilizationData) {
        // Simple response for now
        return new ResponseEntity<>("Fund utilization recorded successfully", HttpStatus.CREATED);
    }
}