package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.DivisionDto;
import com.example.Bright_Aid.service.DivisionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/divisions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DivisionController {

    private final DivisionService divisionService;

    @PostMapping
    public ResponseEntity<DivisionDto> saveDivision(@Valid @RequestBody DivisionDto divisionDto) {
        DivisionDto savedDivision = divisionService.saveDivision(divisionDto);
        return new ResponseEntity<>(savedDivision, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DivisionDto>> getAllDivisions() {
        List<DivisionDto> divisions = divisionService.getAllDivisions();
        return ResponseEntity.ok(divisions);
    }

    @GetMapping("/{divisionId}")
    public ResponseEntity<DivisionDto> getDivisionById(@PathVariable Integer divisionId) {
        DivisionDto division = divisionService.getDivisionById(divisionId);
        return ResponseEntity.ok(division);
    }

    @DeleteMapping("/{divisionId}")
    public ResponseEntity<Void> deleteDivision(@PathVariable Integer divisionId) {
        divisionService.deleteDivision(divisionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{divisionId}/districts")
    public ResponseEntity<List<Integer>> getDistrictIds(@PathVariable Integer divisionId) {
        List<Integer> districtIds = divisionService.getDistrictIds(divisionId);
        return ResponseEntity.ok(districtIds);
    }

    @GetMapping("/{divisionId}/schools")
    public ResponseEntity<List<Integer>> getSchoolIds(@PathVariable Integer divisionId) {
        List<Integer> schoolIds = divisionService.getSchoolIds(divisionId);
        return ResponseEntity.ok(schoolIds);
    }

    @GetMapping("/{divisionId}/donors")
    public ResponseEntity<List<Integer>> getDonorIds(@PathVariable Integer divisionId) {
        List<Integer> donorIds = divisionService.getDonorIds(divisionId);
        return ResponseEntity.ok(donorIds);
    }
}