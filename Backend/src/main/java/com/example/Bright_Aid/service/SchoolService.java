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
    private final DivisionRepository divisionRepository;
    private final DistrictRepository districtRepository;
    private final UpazilaRepository upazilaRepository;

    public SchoolService(SchoolRepository schoolRepository,
                         DivisionRepository divisionRepository,
                         DistrictRepository districtRepository,
                         UpazilaRepository upazilaRepository) {
        this.schoolRepository = schoolRepository;
        this.divisionRepository = divisionRepository;
        this.districtRepository = districtRepository;
        this.upazilaRepository = upazilaRepository;
    }

    // -------------------- CRUD --------------------
    public SchoolDto createSchool(SchoolDto schoolDto) {
        // Validate registration number uniqueness
        if (schoolRepository.existsByRegistrationNumber(schoolDto.getRegistrationNumber())) {
            throw new RuntimeException("Registration number already exists: " + schoolDto.getRegistrationNumber());
        }

        // Validate geographic hierarchy
        validateGeographicHierarchy(schoolDto.getDivisionId(), schoolDto.getDistrictId(), schoolDto.getUpazilaId());

        Division division = divisionRepository.findById(schoolDto.getDivisionId())
                .orElseThrow(() -> new RuntimeException("Division not found with ID: " + schoolDto.getDivisionId()));

        District district = districtRepository.findById(schoolDto.getDistrictId())
                .orElseThrow(() -> new RuntimeException("District not found with ID: " + schoolDto.getDistrictId()));

        Upazila upazila = upazilaRepository.findById(schoolDto.getUpazilaId())
                .orElseThrow(() -> new RuntimeException("Upazila not found with ID: " + schoolDto.getUpazilaId()));

        School school = School.builder()
                .schoolName(schoolDto.getSchoolName())
                .registrationNumber(schoolDto.getRegistrationNumber())
                .schoolType(parseSchoolType(schoolDto.getSchoolType()))
                .address(schoolDto.getAddress())
                .division(division)
                .district(district)
                .upazila(upazila)
                .latitude(schoolDto.getLatitude())
                .longitude(schoolDto.getLongitude())
                .verificationStatus(parseVerificationStatus(schoolDto.getVerificationStatus()))
                .status(parseSchoolStatus(schoolDto.getStatus()))
                .createdAt(LocalDateTime.now())
                .build();

        School saved = schoolRepository.save(school);
        return mapToDto(saved);
    }

    public SchoolDto updateSchool(Integer schoolId, SchoolDto schoolDto) {
        School existingSchool = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new RuntimeException("School not found with ID: " + schoolId));

        // Check if registration number is being changed and if it's unique
        if (!existingSchool.getRegistrationNumber().equals(schoolDto.getRegistrationNumber()) &&
            schoolRepository.existsByRegistrationNumber(schoolDto.getRegistrationNumber())) {
            throw new RuntimeException("Registration number already exists: " + schoolDto.getRegistrationNumber());
        }

        // Validate geographic hierarchy
        validateGeographicHierarchy(schoolDto.getDivisionId(), schoolDto.getDistrictId(), schoolDto.getUpazilaId());

        Division division = divisionRepository.findById(schoolDto.getDivisionId())
                .orElseThrow(() -> new RuntimeException("Division not found with ID: " + schoolDto.getDivisionId()));

        District district = districtRepository.findById(schoolDto.getDistrictId())
                .orElseThrow(() -> new RuntimeException("District not found with ID: " + schoolDto.getDistrictId()));

        Upazila upazila = upazilaRepository.findById(schoolDto.getUpazilaId())
                .orElseThrow(() -> new RuntimeException("Upazila not found with ID: " + schoolDto.getUpazilaId()));

        // Update fields
        existingSchool.setSchoolName(schoolDto.getSchoolName());
        existingSchool.setRegistrationNumber(schoolDto.getRegistrationNumber());
        existingSchool.setSchoolType(parseSchoolType(schoolDto.getSchoolType()));
        existingSchool.setAddress(schoolDto.getAddress());
        existingSchool.setDivision(division);
        existingSchool.setDistrict(district);
        existingSchool.setUpazila(upazila);
        existingSchool.setLatitude(schoolDto.getLatitude());
        existingSchool.setLongitude(schoolDto.getLongitude());
        existingSchool.setUpdatedAt(LocalDateTime.now());

        School updated = schoolRepository.save(existingSchool);
        return mapToDto(updated);
    }

    public List<SchoolDto> getAllSchools() {
        return schoolRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public SchoolDto getSchoolById(Integer schoolId) {
        return schoolRepository.findById(schoolId)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("School not found with ID: " + schoolId));
    }

    public void deleteSchool(Integer schoolId) {
        if (!schoolRepository.existsById(schoolId)) {
            throw new RuntimeException("School not found with ID: " + schoolId);
        }
        schoolRepository.deleteById(schoolId);
    }

    // -------------------- STATUS & VERIFICATION --------------------
    public SchoolDto updateVerificationStatus(Integer schoolId, School.VerificationStatus verificationStatus) {
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new RuntimeException("School not found with ID: " + schoolId));
        school.setVerificationStatus(verificationStatus);
        school.setUpdatedAt(LocalDateTime.now());
        return mapToDto(schoolRepository.save(school));
    }

    public SchoolDto updateSchoolStatus(Integer schoolId, School.SchoolStatus schoolStatus) {
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new RuntimeException("School not found with ID: " + schoolId));
        school.setStatus(schoolStatus);
        school.setUpdatedAt(LocalDateTime.now());
        return mapToDto(schoolRepository.save(school));
    }

    public SchoolDto verifySchool(Integer schoolId) {
        return updateVerificationStatus(schoolId, School.VerificationStatus.VERIFIED);
    }

    public SchoolDto rejectSchool(Integer schoolId) {
        return updateVerificationStatus(schoolId, School.VerificationStatus.REJECTED);
    }

    // -------------------- CUSTOM QUERIES --------------------
    public List<SchoolDto> findSchoolsByName(String name) {
        return schoolRepository.findBySchoolNameContainingIgnoreCase(name)
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<SchoolDto> findSchoolsByStatus(School.SchoolStatus status) {
        return schoolRepository.findByStatus(status)
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<SchoolDto> findSchoolsByVerificationStatus(School.VerificationStatus status) {
        return schoolRepository.findByVerificationStatus(status)
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<SchoolDto> findSchoolsByDivision(Integer divisionId) {
        return schoolRepository.findByDivisionId(divisionId)
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<SchoolDto> findSchoolsByDistrict(Integer districtId) {
        return schoolRepository.findByDistrictId(districtId)
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    // -------------------- HELPER METHODS --------------------
    private void validateGeographicHierarchy(Integer divisionId, Integer districtId, Integer upazilaId) {
        if (divisionId == null || districtId == null || upazilaId == null) {
            throw new RuntimeException("Division, District, and Upazila IDs are required");
        }

        // You can add more specific validation here to ensure district belongs to division, etc.
    }

    private School.SchoolType parseSchoolType(String schoolType) {
        try {
            return School.SchoolType.valueOf(schoolType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid school type: " + schoolType);
        }
    }

    private School.VerificationStatus parseVerificationStatus(String verificationStatus) {
        try {
            return School.VerificationStatus.valueOf(verificationStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            return School.VerificationStatus.PENDING;
        }
    }

    private School.SchoolStatus parseSchoolStatus(String schoolStatus) {
        try {
            return School.SchoolStatus.valueOf(schoolStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            return School.SchoolStatus.ACTIVE;
        }
    }

    // -------------------- Mapping --------------------
    private SchoolDto mapToDto(School school) {
        return SchoolDto.builder()
                .schoolId(school.getSchoolId())
                .schoolName(school.getSchoolName())
                .registrationNumber(school.getRegistrationNumber())
                .schoolType(school.getSchoolType().name())
                .address(school.getAddress())
                .divisionId(school.getDivision() != null ? school.getDivision().getDivisionId() : null)
                .districtId(school.getDistrict() != null ? school.getDistrict().getDistrictId() : null)
                .upazilaId(school.getUpazila() != null ? school.getUpazila().getUpazilaId() : null)
                .latitude(school.getLatitude())
                .longitude(school.getLongitude())
                .verificationStatus(school.getVerificationStatus().name())
                .status(school.getStatus().name())
                .createdAt(school.getCreatedAt())
                .updatedAt(school.getUpdatedAt())
                .build();
    }
}