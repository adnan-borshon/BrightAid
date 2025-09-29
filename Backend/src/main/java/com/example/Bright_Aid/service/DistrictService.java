package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Entity.District;
import com.example.Bright_Aid.Entity.Division;
import com.example.Bright_Aid.Entity.School;
import com.example.Bright_Aid.Entity.Upazila;
import com.example.Bright_Aid.Dto.DistrictDto;
import com.example.Bright_Aid.repository.DistrictRepository;
import com.example.Bright_Aid.repository.DivisionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DistrictService {

    private final DistrictRepository districtRepository;
    private final DivisionRepository divisionRepository;

    public DistrictService(DistrictRepository districtRepository, DivisionRepository divisionRepository) {
        this.districtRepository = districtRepository;
        this.divisionRepository = divisionRepository;
    }

    // Create or update District
    public DistrictDto saveDistrict(DistrictDto districtDto) {
        Division division = divisionRepository.findById(districtDto.getDivisionId())
                .orElseThrow(() -> new RuntimeException("Division not found"));

        District district = District.builder()
                .districtId(districtDto.getDistrictId())
                .districtName(districtDto.getDistrictName())
                .districtCode(districtDto.getDistrictCode())
                .division(division)
                .build();

        District saved = districtRepository.save(district);
        return mapToDto(saved);
    }

    // Get all districts
    public List<DistrictDto> getAllDistricts() {
        return districtRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Get district by ID
    public DistrictDto getDistrictById(Integer districtId) {
        return districtRepository.findById(districtId)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("District not found"));
    }

    // Delete district
    public void deleteDistrict(Integer districtId) {
        if (!districtRepository.existsById(districtId)) {
            throw new RuntimeException("District not found");
        }
        districtRepository.deleteById(districtId);
    }

    // Get all upazilas for a district
    public List<Integer> getUpazilaIds(Integer districtId) {
        District district = districtRepository.findById(districtId)
                .orElseThrow(() -> new RuntimeException("District not found"));
        List<Upazila> upazilas = district.getUpazilas();
        if (upazilas == null) return List.of();
        return upazilas.stream().map(Upazila::getUpazilaId).collect(Collectors.toList());
    }

    // Get all schools for a district
    public List<Integer> getSchoolIds(Integer districtId) {
        District district = districtRepository.findById(districtId)
                .orElseThrow(() -> new RuntimeException("District not found"));
        List<School> schools = district.getSchools();
        if (schools == null) return List.of();
        return schools.stream().map(School::getSchoolId).collect(Collectors.toList());
    }

    // Map District entity to DTO
    private DistrictDto mapToDto(District district) {
        List<Integer> upazilaIds = district.getUpazilas() != null ?
                district.getUpazilas().stream().map(Upazila::getUpazilaId).toList() :
                List.of();

        List<Integer> schoolIds = district.getSchools() != null ?
                district.getSchools().stream().map(School::getSchoolId).toList() :
                List.of();

        return DistrictDto.builder()
                .districtId(district.getDistrictId())
                .districtName(district.getDistrictName())
                .districtCode(district.getDistrictCode())
                .divisionId(district.getDivision().getDivisionId())
                .divisionName(district.getDivision().getDivisionName())
                .upazilaIds(upazilaIds)
                .schoolIds(schoolIds)
                .build();
    }
}