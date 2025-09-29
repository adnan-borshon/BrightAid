package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Dto.UpazilaDto;
import com.example.Bright_Aid.Entity.District;
import com.example.Bright_Aid.Entity.School;
import com.example.Bright_Aid.Entity.Upazila;
import com.example.Bright_Aid.repository.DistrictRepository;
import com.example.Bright_Aid.repository.UpazilaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UpazilaService {

    private final UpazilaRepository upazilaRepository;
    private final DistrictRepository districtRepository;

    @Transactional
    public UpazilaDto createUpazila(UpazilaDto dto) {
        District district = districtRepository.findById(dto.getDistrictId())
                .orElseThrow(() -> new RuntimeException("District not found with id: " + dto.getDistrictId()));

        Upazila upazila = Upazila.builder()
                .district(district)
                .upazilaName(dto.getUpazilaName())
                .upazilaCode(dto.getUpazilaCode())
                .build();

        Upazila saved = upazilaRepository.save(upazila);
        return convertToDto(saved);
    }

    @Transactional(readOnly = true)
    public UpazilaDto getUpazilaById(Integer id) {
        Upazila upazila = upazilaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Upazila not found with id: " + id));
        return convertToDto(upazila);
    }

    @Transactional(readOnly = true)
    public List<UpazilaDto> getAllUpazilas() {
        return upazilaRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public UpazilaDto updateUpazila(Integer id, UpazilaDto dto) {
        Upazila upazila = upazilaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Upazila not found with id: " + id));

        District district = districtRepository.findById(dto.getDistrictId())
                .orElseThrow(() -> new RuntimeException("District not found with id: " + dto.getDistrictId()));

        upazila.setDistrict(district);
        upazila.setUpazilaName(dto.getUpazilaName());
        upazila.setUpazilaCode(dto.getUpazilaCode());

        Upazila updated = upazilaRepository.save(upazila);
        return convertToDto(updated);
    }

    @Transactional
    public void deleteUpazila(Integer id) {
        if (!upazilaRepository.existsById(id)) {
            throw new RuntimeException("Upazila not found with id: " + id);
        }
        upazilaRepository.deleteById(id);
    }

    private UpazilaDto convertToDto(Upazila upazila) {
        List<Integer> schoolIds = null;
        if (upazila.getSchools() != null) {
            schoolIds = upazila.getSchools().stream()
                    .map(School::getSchoolId)
                    .collect(Collectors.toList());
        }

        return UpazilaDto.builder()
                .upazilaId(upazila.getUpazilaId())
                .districtId(upazila.getDistrict().getDistrictId())
                .upazilaName(upazila.getUpazilaName())
                .upazilaCode(upazila.getUpazilaCode())
                .schoolIds(schoolIds)
                .build();
    }
}