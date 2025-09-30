package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.SchoolDto;
import com.example.Bright_Aid.Entity.School;
import com.example.Bright_Aid.service.SchoolService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schools")
public class SchoolController {

    private final SchoolService schoolService;

    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    // -------------------- CRUD --------------------

    // Create new school
    // -------------------- AUTO-CREATE ENDPOINTS --------------------



    // Regular create (requires all foreign keys to exist)
    @PostMapping
    public ResponseEntity<SchoolDto> createSchool(@Valid @RequestBody SchoolDto schoolDto) {
        SchoolDto createdSchool = schoolService.saveSchool(schoolDto);
        return new ResponseEntity<>(createdSchool, HttpStatus.CREATED);
    }

    // Get all schools
    @GetMapping
    public ResponseEntity<List<SchoolDto>> getAllSchools() {
        List<SchoolDto> schools = schoolService.getAllSchools();
        return new ResponseEntity<>(schools, HttpStatus.OK);
    }

    // Get school by ID
    @GetMapping("/{schoolId}")
    public ResponseEntity<SchoolDto> getSchoolById(@PathVariable Integer schoolId) {
        SchoolDto school = schoolService.getSchoolById(schoolId);
        return new ResponseEntity<>(school, HttpStatus.OK);
    }

    // Update school
    @PutMapping("/{schoolId}")
    public ResponseEntity<SchoolDto> updateSchool(@PathVariable Integer schoolId,
                                                  @Valid @RequestBody SchoolDto schoolDto) {
        schoolDto.setSchoolId(schoolId);
        SchoolDto updatedSchool = schoolService.saveSchool(schoolDto);
        return new ResponseEntity<>(updatedSchool, HttpStatus.OK);
    }

    // Delete school
    @DeleteMapping("/{schoolId}")
    public ResponseEntity<Void> deleteSchool(@PathVariable Integer schoolId) {
        schoolService.deleteSchool(schoolId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // -------------------- STATUS & VERIFICATION --------------------

    // Update verification status
    @PatchMapping("/{schoolId}/verification-status")
    public ResponseEntity<SchoolDto> updateVerificationStatus(@PathVariable Integer schoolId,
                                                              @RequestParam String verificationStatus) {
        School.VerificationStatus status = School.VerificationStatus.valueOf(verificationStatus.toUpperCase());
        SchoolDto updatedSchool = schoolService.updateVerificationStatus(schoolId, status);
        return new ResponseEntity<>(updatedSchool, HttpStatus.OK);
    }

    // Update school status
    @PatchMapping("/{schoolId}/status")
    public ResponseEntity<SchoolDto> updateSchoolStatus(@PathVariable Integer schoolId,
                                                        @RequestParam String status) {
        School.SchoolStatus schoolStatus = School.SchoolStatus.valueOf(status.toUpperCase());
        SchoolDto updatedSchool = schoolService.updateSchoolStatus(schoolId, schoolStatus);
        return new ResponseEntity<>(updatedSchool, HttpStatus.OK);
    }

    // Verify school
    @PatchMapping("/{schoolId}/verify")
    public ResponseEntity<SchoolDto> verifySchool(@PathVariable Integer schoolId) {
        SchoolDto verifiedSchool = schoolService.verifySchool(schoolId);
        return new ResponseEntity<>(verifiedSchool, HttpStatus.OK);
    }

    // Reject school verification
    @PatchMapping("/{schoolId}/reject")
    public ResponseEntity<SchoolDto> rejectSchool(@PathVariable Integer schoolId) {
        SchoolDto rejectedSchool = schoolService.rejectSchool(schoolId);
        return new ResponseEntity<>(rejectedSchool, HttpStatus.OK);
    }

    // -------------------- CUSTOM QUERY ENDPOINTS --------------------

    // Get schools by name (partial match)
    @GetMapping("/search")
    public ResponseEntity<List<SchoolDto>> searchSchoolsByName(@RequestParam String name) {
        List<SchoolDto> schools = schoolService.findSchoolsByName(name);
        return new ResponseEntity<>(schools, HttpStatus.OK);
    }

    // Get schools by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<SchoolDto>> getSchoolsByStatus(@PathVariable String status) {
        School.SchoolStatus schoolStatus = School.SchoolStatus.valueOf(status.toUpperCase());
        List<SchoolDto> schools = schoolService.findSchoolsByStatus(schoolStatus);
        return new ResponseEntity<>(schools, HttpStatus.OK);
    }

    // Get schools by verification status
    @GetMapping("/verification/{status}")
    public ResponseEntity<List<SchoolDto>> getSchoolsByVerificationStatus(@PathVariable String status) {
        School.VerificationStatus verificationStatus = School.VerificationStatus.valueOf(status.toUpperCase());
        List<SchoolDto> schools = schoolService.findSchoolsByVerificationStatus(verificationStatus);
        return new ResponseEntity<>(schools, HttpStatus.OK);
    }
}
