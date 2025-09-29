package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Entity.*;
import com.example.Bright_Aid.Dto.SchoolDto;
import com.example.Bright_Aid.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SchoolService {

    private final SchoolRepository schoolRepository;
    private final UserRepository userRepository;
    private final DivisionRepository divisionRepository;
    private final DistrictRepository districtRepository;
    private final com.example.Bright_Aid.repository.UpazilaRepository upazilaRepository;

    public SchoolService(SchoolRepository schoolRepository,
                         UserRepository userRepository,
                         DivisionRepository divisionRepository,
                         DistrictRepository districtRepository,
                         com.example.Bright_Aid.repository.UpazilaRepository upazilaRepository) {
        this.schoolRepository = schoolRepository;
        this.userRepository = userRepository;
        this.divisionRepository = divisionRepository;
        this.districtRepository = districtRepository;
        this.upazilaRepository = upazilaRepository;
    }

    // Create or update School
    public SchoolDto saveSchool(SchoolDto schoolDto) {
        if (schoolDto.getUserId() == null || schoolDto.getUserId() <= 0) {
            throw new RuntimeException("Valid User ID is required");
        }
        
        User user = userRepository.findById(schoolDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + schoolDto.getUserId()));

        Division division = null;
        District district = null;
        Upazila upazila = null;
        
        if (schoolDto.getDivisionId() != null) {
            division = divisionRepository.findById(schoolDto.getDivisionId())
                    .orElse(null);
        }
        
        if (schoolDto.getDistrictId() != null) {
            district = districtRepository.findById(schoolDto.getDistrictId())
                    .orElse(null);
        }
        
        if (schoolDto.getUpazilaId() != null) {
            upazila = upazilaRepository.findById(schoolDto.getUpazilaId())
                    .orElse(null);
        }

        School school = School.builder()
                .user(user)
                .schoolName(schoolDto.getSchoolName())
                .registrationNumber(schoolDto.getRegistrationNumber())
                .schoolType(School.SchoolType.valueOf(schoolDto.getSchoolType().toUpperCase()))
                .address(schoolDto.getAddress())
                .division(division)
                .district(district)
                .upazila(upazila)
                .latitude(schoolDto.getLatitude())
                .longitude(schoolDto.getLongitude())
                .verificationStatus(schoolDto.getVerificationStatus() != null ?
                        School.VerificationStatus.valueOf(schoolDto.getVerificationStatus()) : School.VerificationStatus.PENDING)
                .status(schoolDto.getStatus() != null ?
                        School.SchoolStatus.valueOf(schoolDto.getStatus()) : School.SchoolStatus.ACTIVE)
                .build();

        School saved = schoolRepository.save(school);
        return mapToDto(saved);
    }

    // Get all schools
    public List<SchoolDto> getAllSchools() {
        return schoolRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Get school by ID
    public SchoolDto getSchoolById(Integer schoolId) {
        return schoolRepository.findById(schoolId)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("School not found"));
    }

    // Delete school
    public void deleteSchool(Integer schoolId) {
        if (!schoolRepository.existsById(schoolId)) {
            throw new RuntimeException("School not found");
        }
        schoolRepository.deleteById(schoolId);
    }

    // Update verification status
    public SchoolDto updateVerificationStatus(Integer schoolId, School.VerificationStatus verificationStatus) {
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new RuntimeException("School not found"));

        school.setVerificationStatus(verificationStatus);
        School saved = schoolRepository.save(school);
        return mapToDto(saved);
    }

    // Update school status
    public SchoolDto updateSchoolStatus(Integer schoolId, School.SchoolStatus schoolStatus) {
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new RuntimeException("School not found"));

        school.setStatus(schoolStatus);
        School saved = schoolRepository.save(school);
        return mapToDto(saved);
    }

    // Verify school
    public SchoolDto verifySchool(Integer schoolId) {
        return updateVerificationStatus(schoolId, School.VerificationStatus.VERIFIED);
    }

    // Reject school verification
    public SchoolDto rejectSchool(Integer schoolId) {
        return updateVerificationStatus(schoolId, School.VerificationStatus.REJECTED);
    }

    // Map School entity to DTO
    private SchoolDto mapToDto(School school) {
        return SchoolDto.builder()
                .schoolId(school.getSchoolId())
                .userId(school.getUser().getUserId())
                .schoolName(school.getSchoolName())
                .registrationNumber(school.getRegistrationNumber())
                .schoolType(school.getSchoolType().name())
                .address(school.getAddress())
                .divisionId(school.getDivision().getDivisionId())
                .districtId(school.getDistrict().getDistrictId())
                .upazilaId(school.getUpazila().getUpazilaId())
                .latitude(school.getLatitude())
                .longitude(school.getLongitude())
                .verificationStatus(school.getVerificationStatus().name())
                .status(school.getStatus().name())
                .build();
    }
}