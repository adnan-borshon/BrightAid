package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Entity.District;
import com.example.Bright_Aid.Entity.Division;
import com.example.Bright_Aid.Entity.Donor;
import com.example.Bright_Aid.Entity.School;
import com.example.Bright_Aid.Dto.DivisionDto;
import com.example.Bright_Aid.repository.DivisionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DivisionService {

    private final DivisionRepository divisionRepository;

    public DivisionService(DivisionRepository divisionRepository) {
        this.divisionRepository = divisionRepository;
    }

    // ✅ Insert all divisions of Bangladesh (custom query)
    public void insertDefaultBangladeshDivisions() {
        divisionRepository.insertAllBangladeshDivisions();
    }

    // Create or update Division
    public DivisionDto saveDivision(DivisionDto divisionDto) {
        Division division = Division.builder()
                .divisionId(divisionDto.getDivisionId())
                .divisionName(divisionDto.getDivisionName())
                .divisionCode(divisionDto.getDivisionCode())
                .build();

        Division saved = divisionRepository.save(division);
        return mapToDto(saved);
    }

    // Get all divisions
    public List<DivisionDto> getAllDivisions() {
        return divisionRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Get division by ID
    public DivisionDto getDivisionById(Integer divisionId) {
        return divisionRepository.findById(divisionId)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Division not found"));
    }

    // Delete division
    public void deleteDivision(Integer divisionId) {
        if (!divisionRepository.existsById(divisionId)) {
            throw new RuntimeException("Division not found");
        }
        divisionRepository.deleteById(divisionId);
    }

    // Get all districts for a division
    public List<Integer> getDistrictIds(Integer divisionId) {
        Division division = divisionRepository.findById(divisionId)
                .orElseThrow(() -> new RuntimeException("Division not found"));
        List<District> districts = division.getDistricts();
        if (districts == null) return List.of();
        return districts.stream().map(District::getDistrictId).collect(Collectors.toList());
    }

    // Get all schools for a division
    public List<Integer> getSchoolIds(Integer divisionId) {
        Division division = divisionRepository.findById(divisionId)
                .orElseThrow(() -> new RuntimeException("Division not found"));
        List<School> schools = division.getSchools();
        if (schools == null) return List.of();
        return schools.stream().map(School::getSchoolId).collect(Collectors.toList());
    }

    // Get all donors for a division
    public List<Integer> getDonorIds(Integer divisionId) {
        Division division = divisionRepository.findById(divisionId)
                .orElseThrow(() -> new RuntimeException("Division not found"));
        List<Donor> donors = division.getDonors();
        if (donors == null) return List.of();
        return donors.stream().map(Donor::getDonorId).collect(Collectors.toList());
    }

    // Map Division entity to DTO
    private DivisionDto mapToDto(Division division) {
        List<Integer> districtIds = division.getDistricts() != null ?
                division.getDistricts().stream().map(District::getDistrictId).toList() :
                List.of();

        List<Integer> schoolIds = division.getSchools() != null ?
                division.getSchools().stream().map(School::getSchoolId).toList() :
                List.of();

        List<Integer> donorIds = division.getDonors() != null ?
                division.getDonors().stream().map(Donor::getDonorId).toList() :
                List.of();

        return DivisionDto.builder()
                .divisionId(division.getDivisionId())
                .divisionName(division.getDivisionName())
                .divisionCode(division.getDivisionCode())
                .districtIds(districtIds)
                .schoolIds(schoolIds)
                .donorIds(donorIds)
                .build();
    }
}
