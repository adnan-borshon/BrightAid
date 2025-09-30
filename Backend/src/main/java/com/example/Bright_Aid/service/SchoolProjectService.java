package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Entity.*;
import com.example.Bright_Aid.Dto.SchoolProjectDto;
import com.example.Bright_Aid.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SchoolProjectService {

    private final SchoolProjectRepository schoolProjectRepository;
    private final SchoolRepository schoolRepository;
    private final ProjectTypeRepository projectTypeRepository;

    public SchoolProjectService(SchoolProjectRepository schoolProjectRepository,
                                SchoolRepository schoolRepository,
                                ProjectTypeRepository projectTypeRepository) {
        this.schoolProjectRepository = schoolProjectRepository;
        this.schoolRepository = schoolRepository;
        this.projectTypeRepository = projectTypeRepository;
    }

    // Create or update SchoolProject
    public SchoolProjectDto saveSchoolProject(SchoolProjectDto schoolProjectDto) {
        School school = schoolRepository.findById(schoolProjectDto.getSchoolId())
                .orElseThrow(() -> new RuntimeException("School not found"));

        ProjectType projectType = projectTypeRepository.findById(schoolProjectDto.getProjectTypeId())
                .orElseThrow(() -> new RuntimeException("Project type not found"));

        SchoolProject schoolProject = SchoolProject.builder()
                .projectId(schoolProjectDto.getProjectId())
                .school(school)
                .projectTitle(schoolProjectDto.getProjectTitle())
                .projectDescription(schoolProjectDto.getProjectDescription())
                .projectType(projectType)
                .build();

        SchoolProject saved = schoolProjectRepository.save(schoolProject);
        return mapToDto(saved);
    }

    // Get all school projects
    public List<SchoolProjectDto> getAllSchoolProjects() {
        return schoolProjectRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Get school project by ID
    public SchoolProjectDto getSchoolProjectById(Integer projectId) {
        return schoolProjectRepository.findById(projectId)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("School project not found"));
    }

    // Delete school project
    public void deleteSchoolProject(Integer projectId) {
        if (!schoolProjectRepository.existsById(projectId)) {
            throw new RuntimeException("School project not found");
        }
        schoolProjectRepository.deleteById(projectId);
    }

    // Map SchoolProject entity to DTO
    private SchoolProjectDto mapToDto(SchoolProject schoolProject) {
        return SchoolProjectDto.builder()
                .projectId(schoolProject.getProjectId())
                .schoolId(schoolProject.getSchool().getSchoolId())
                .projectTitle(schoolProject.getProjectTitle())
                .projectDescription(schoolProject.getProjectDescription())
                .projectTypeId(schoolProject.getProjectType().getProjectTypeId())
                .createdAt(schoolProject.getCreatedAt())
                .updatedAt(schoolProject.getUpdatedAt())
                .build();
    }
}