package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Dto.ProjectTypeDto;
import com.example.Bright_Aid.Entity.ProjectType;
import com.example.Bright_Aid.repository.ProjectTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProjectTypeService {

    private final ProjectTypeRepository projectTypeRepository;

    @Transactional
    public ProjectTypeDto createProjectType(ProjectTypeDto projectTypeDto) {
        log.info("Creating project type with name: {}", projectTypeDto.getTypeName());

        ProjectType projectType = ProjectType.builder()
                .typeName(projectTypeDto.getTypeName())
                .typeCode(projectTypeDto.getTypeCode())
                .typeDescription(projectTypeDto.getTypeDescription())
                .isActive(projectTypeDto.getIsActive())
                .build();

        ProjectType savedProjectType = projectTypeRepository.save(projectType);
        log.info("Successfully created project type with ID: {}", savedProjectType.getProjectTypeId());

        return convertToDto(savedProjectType);
    }

    public ProjectTypeDto getProjectTypeById(Integer projectTypeId) {
        log.info("Fetching project type with ID: {}", projectTypeId);

        ProjectType projectType = projectTypeRepository.findById(projectTypeId)
                .orElseThrow(() -> new RuntimeException("Project type not found with ID: " + projectTypeId));

        return convertToDto(projectType);
    }

    public List<ProjectTypeDto> getAllProjectTypes() {
        log.info("Fetching all project types");

        List<ProjectType> projectTypes = projectTypeRepository.findAll();
        return projectTypes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<ProjectTypeDto> getAllProjectTypes(Pageable pageable) {
        log.info("Fetching project types with pagination: {}", pageable);

        Page<ProjectType> projectTypes = projectTypeRepository.findAll(pageable);
        return projectTypes.map(this::convertToDto);
    }

    @Transactional
    public ProjectTypeDto updateProjectType(Integer projectTypeId, ProjectTypeDto projectTypeDto) {
        log.info("Updating project type with ID: {}", projectTypeId);

        ProjectType existingProjectType = projectTypeRepository.findById(projectTypeId)
                .orElseThrow(() -> new RuntimeException("Project type not found with ID: " + projectTypeId));

        if (projectTypeDto.getTypeName() != null) {
            existingProjectType.setTypeName(projectTypeDto.getTypeName());
        }
        if (projectTypeDto.getTypeCode() != null) {
            existingProjectType.setTypeCode(projectTypeDto.getTypeCode());
        }
        if (projectTypeDto.getTypeDescription() != null) {
            existingProjectType.setTypeDescription(projectTypeDto.getTypeDescription());
        }
        if (projectTypeDto.getIsActive() != null) {
            existingProjectType.setIsActive(projectTypeDto.getIsActive());
        }

        ProjectType updatedProjectType = projectTypeRepository.save(existingProjectType);
        log.info("Successfully updated project type with ID: {}", projectTypeId);

        return convertToDto(updatedProjectType);
    }

    @Transactional
    public void deleteProjectType(Integer projectTypeId) {
        log.info("Deleting project type with ID: {}", projectTypeId);

        ProjectType projectType = projectTypeRepository.findById(projectTypeId)
                .orElseThrow(() -> new RuntimeException("Project type not found with ID: " + projectTypeId));

        projectTypeRepository.deleteById(projectTypeId);
        log.info("Successfully deleted project type with ID: {}", projectTypeId);
    }

    private ProjectTypeDto convertToDto(ProjectType projectType) {
        List<Integer> projectIds = new ArrayList<>();

        if (projectType.getProjects() != null && !projectType.getProjects().isEmpty()) {
            projectIds = projectType.getProjects().stream()
                    .map(project -> project.getNgoProjectId()) // Adjust method name as needed
                    .collect(Collectors.toList());
        }

        return ProjectTypeDto.builder()
                .projectTypeId(projectType.getProjectTypeId())
                .typeName(projectType.getTypeName())
                .typeCode(projectType.getTypeCode())
                .typeDescription(projectType.getTypeDescription())
                .isActive(projectType.getIsActive())
                .projectIds(projectIds)
                .build();
    }
}