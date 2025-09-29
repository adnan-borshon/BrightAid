package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Dto.NgoProjectSchoolDto;
import com.example.Bright_Aid.Entity.*;
import com.example.Bright_Aid.repository.NgoProjectSchoolRepository;
import com.example.Bright_Aid.repository.NgoProjectRepository;
import com.example.Bright_Aid.repository.SchoolRepository;
import com.example.Bright_Aid.repository.ProjectTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NgoProjectSchoolService {

    private final NgoProjectSchoolRepository ngoProjectSchoolRepository;
    private final NgoProjectRepository ngoProjectRepository;
    private final SchoolRepository schoolRepository;
    private final ProjectTypeRepository projectTypeRepository;

    public List<NgoProjectSchoolDto> getAllNgoProjectSchools() {
        log.info("Fetching all NGO project schools");
        return ngoProjectSchoolRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<NgoProjectSchoolDto> getAllNgoProjectSchools(Pageable pageable) {
        log.info("Fetching all NGO project schools with pagination");
        return ngoProjectSchoolRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    public Optional<NgoProjectSchoolDto> getNgoProjectSchoolById(Integer npsId) {
        log.info("Fetching NGO project school with ID: {}", npsId);
        return ngoProjectSchoolRepository.findById(npsId)
                .map(this::convertToDto);
    }

    public NgoProjectSchoolDto createNgoProjectSchool(NgoProjectSchoolDto ngoProjectSchoolDto) {
        log.info("Creating new NGO project school");

        NgoProjectSchool ngoProjectSchool = convertToEntity(ngoProjectSchoolDto);
        ngoProjectSchool.setJoinedAt(LocalDateTime.now());

        NgoProjectSchool savedNgoProjectSchool = ngoProjectSchoolRepository.save(ngoProjectSchool);
        log.info("Created NGO project school with ID: {}", savedNgoProjectSchool.getNpsId());

        return convertToDto(savedNgoProjectSchool);
    }

    public Optional<NgoProjectSchoolDto> updateNgoProjectSchool(Integer npsId, NgoProjectSchoolDto ngoProjectSchoolDto) {
        log.info("Updating NGO project school with ID: {}", npsId);

        return ngoProjectSchoolRepository.findById(npsId)
                .map(existingNgoProjectSchool -> {
                    updateEntityFromDto(existingNgoProjectSchool, ngoProjectSchoolDto);
                    NgoProjectSchool updatedNgoProjectSchool = ngoProjectSchoolRepository.save(existingNgoProjectSchool);
                    log.info("Updated NGO project school with ID: {}", npsId);
                    return convertToDto(updatedNgoProjectSchool);
                });
    }

    public boolean deleteNgoProjectSchool(Integer npsId) {
        log.info("Deleting NGO project school with ID: {}", npsId);

        if (ngoProjectSchoolRepository.existsById(npsId)) {
            ngoProjectSchoolRepository.deleteById(npsId);
            log.info("Deleted NGO project school with ID: {}", npsId);
            return true;
        }

        log.warn("NGO project school with ID {} not found for deletion", npsId);
        return false;
    }

    public List<NgoProjectSchoolDto> getNgoProjectSchoolsByNgoProjectId(Integer ngoProjectId) {
        log.info("Fetching NGO project schools for NGO project ID: {}", ngoProjectId);
        return ngoProjectSchoolRepository.findAll()
                .stream()
                .filter(nps -> nps.getNgoProject().getNgoProjectId().equals(ngoProjectId))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<NgoProjectSchoolDto> getNgoProjectSchoolsBySchoolId(Integer schoolId) {
        log.info("Fetching NGO project schools for school ID: {}", schoolId);
        return ngoProjectSchoolRepository.findAll()
                .stream()
                .filter(nps -> nps.getSchool().getSchoolId().equals(schoolId))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<NgoProjectSchoolDto> getNgoProjectSchoolsByParticipationStatus(String participationStatus) {
        log.info("Fetching NGO project schools with participation status: {}", participationStatus);
        NgoProjectSchool.ParticipationStatus status = NgoProjectSchool.ParticipationStatus.valueOf(participationStatus.toUpperCase());
        return ngoProjectSchoolRepository.findAll()
                .stream()
                .filter(nps -> nps.getParticipationStatus().equals(status))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private NgoProjectSchoolDto convertToDto(NgoProjectSchool ngoProjectSchool) {
        BigDecimal remainingBudget = null;
        Double budgetUtilizationPercentage = 0.0;

        if (ngoProjectSchool.getAllocatedBudget() != null) {
            BigDecimal allocated = ngoProjectSchool.getAllocatedBudget();
            BigDecimal utilized = ngoProjectSchool.getUtilizedBudget() != null ?
                    ngoProjectSchool.getUtilizedBudget() : BigDecimal.ZERO;

            remainingBudget = allocated.subtract(utilized);

            if (allocated.compareTo(BigDecimal.ZERO) > 0) {
                budgetUtilizationPercentage = utilized.divide(allocated, 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100")).doubleValue();
            }
        }

        return NgoProjectSchoolDto.builder()
                .npsId(ngoProjectSchool.getNpsId())
                .ngoProjectId(ngoProjectSchool.getNgoProject().getNgoProjectId())
                .ngoName(ngoProjectSchool.getNgoProject().getNgo().getNgoName())
                .schoolId(ngoProjectSchool.getSchool().getSchoolId())
                .schoolName(ngoProjectSchool.getSchool().getSchoolName())
                .schoolAddress(ngoProjectSchool.getSchool().getAddress())
                .projectNameForSchool(ngoProjectSchool.getProjectNameForSchool())
                .projectTypeId(ngoProjectSchool.getProjectType().getProjectTypeId())
                .projectTypeName(ngoProjectSchool.getProjectType().getTypeName())
                .projectTypeDescription(ngoProjectSchool.getProjectType().getTypeDescription())
                .projectDescriptionForSchool(ngoProjectSchool.getProjectDescriptionForSchool())
                .participationStatus(ngoProjectSchool.getParticipationStatus().name())
                .allocatedBudget(ngoProjectSchool.getAllocatedBudget())
                .utilizedBudget(ngoProjectSchool.getUtilizedBudget())
                .joinedAt(ngoProjectSchool.getJoinedAt())
                .remainingBudget(remainingBudget)
                .budgetUtilizationPercentage(budgetUtilizationPercentage)
                .build();
    }

    private NgoProjectSchool convertToEntity(NgoProjectSchoolDto dto) {
        NgoProject ngoProject = ngoProjectRepository.findById(dto.getNgoProjectId())
                .orElseThrow(() -> new RuntimeException("NGO Project not found with ID: " + dto.getNgoProjectId()));

        School school = schoolRepository.findById(dto.getSchoolId())
                .orElseThrow(() -> new RuntimeException("School not found with ID: " + dto.getSchoolId()));

        ProjectType projectType = projectTypeRepository.findById(dto.getProjectTypeId())
                .orElseThrow(() -> new RuntimeException("Project Type not found with ID: " + dto.getProjectTypeId()));

        return NgoProjectSchool.builder()
                .ngoProject(ngoProject)
                .school(school)
                .projectNameForSchool(dto.getProjectNameForSchool())
                .projectType(projectType)
                .projectDescriptionForSchool(dto.getProjectDescriptionForSchool())
                .participationStatus(NgoProjectSchool.ParticipationStatus.valueOf(dto.getParticipationStatus().toUpperCase()))
                .allocatedBudget(dto.getAllocatedBudget())
                .utilizedBudget(dto.getUtilizedBudget())
                .build();
    }

    private void updateEntityFromDto(NgoProjectSchool entity, NgoProjectSchoolDto dto) {
        if (dto.getNgoProjectId() != null && !dto.getNgoProjectId().equals(entity.getNgoProject().getNgoProjectId())) {
            NgoProject ngoProject = ngoProjectRepository.findById(dto.getNgoProjectId())
                    .orElseThrow(() -> new RuntimeException("NGO Project not found with ID: " + dto.getNgoProjectId()));
            entity.setNgoProject(ngoProject);
        }

        if (dto.getSchoolId() != null && !dto.getSchoolId().equals(entity.getSchool().getSchoolId())) {
            School school = schoolRepository.findById(dto.getSchoolId())
                    .orElseThrow(() -> new RuntimeException("School not found with ID: " + dto.getSchoolId()));
            entity.setSchool(school);
        }

        if (dto.getProjectTypeId() != null && !dto.getProjectTypeId().equals(entity.getProjectType().getProjectTypeId())) {
            ProjectType projectType = projectTypeRepository.findById(dto.getProjectTypeId())
                    .orElseThrow(() -> new RuntimeException("Project Type not found with ID: " + dto.getProjectTypeId()));
            entity.setProjectType(projectType);
        }

        if (dto.getProjectNameForSchool() != null) {
            entity.setProjectNameForSchool(dto.getProjectNameForSchool());
        }

        if (dto.getProjectDescriptionForSchool() != null) {
            entity.setProjectDescriptionForSchool(dto.getProjectDescriptionForSchool());
        }

        if (dto.getParticipationStatus() != null) {
            entity.setParticipationStatus(NgoProjectSchool.ParticipationStatus.valueOf(dto.getParticipationStatus().toUpperCase()));
        }

        if (dto.getAllocatedBudget() != null) {
            entity.setAllocatedBudget(dto.getAllocatedBudget());
        }

        if (dto.getUtilizedBudget() != null) {
            entity.setUtilizedBudget(dto.getUtilizedBudget());
        }
    }
}