package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.DistrictDto;
import com.example.Bright_Aid.service.DistrictService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/districts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DistrictController {

    private final DistrictService districtService;

    @PostMapping
    public ResponseEntity<DistrictDto> saveDistrict(@Valid @RequestBody DistrictDto districtDto) {
        DistrictDto savedDistrict = districtService.saveDistrict(districtDto);
        return new ResponseEntity<>(savedDistrict, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DistrictDto>> getAllDistricts() {
        List<DistrictDto> districts = districtService.getAllDistricts();
        return ResponseEntity.ok(districts);
    }

    @GetMapping("/{districtId}")
    public ResponseEntity<DistrictDto> getDistrictById(@PathVariable Integer districtId) {
        DistrictDto district = districtService.getDistrictById(districtId);
        return ResponseEntity.ok(district);
    }

    @DeleteMapping("/{districtId}")
    public ResponseEntity<Void> deleteDistrict(@PathVariable Integer districtId) {
        districtService.deleteDistrict(districtId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{districtId}/upazilas")
    public ResponseEntity<List<Integer>> getUpazilaIds(@PathVariable Integer districtId) {
        List<Integer> upazilaIds = districtService.getUpazilaIds(districtId);
        return ResponseEntity.ok(upazilaIds);
    }

    @GetMapping("/{districtId}/schools")
    public ResponseEntity<List<Integer>> getSchoolIds(@PathVariable Integer districtId) {
        List<Integer> schoolIds = districtService.getSchoolIds(districtId);
        return ResponseEntity.ok(schoolIds);
    }
}