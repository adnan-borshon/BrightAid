package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Entity.*;
import com.example.Bright_Aid.Dto.SchoolProjectDto;
import com.example.Bright_Aid.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.annotation.PostConstruct;

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

    @PostConstruct
    public void initializeProjectTypes() {
        try {
            long count = projectTypeRepository.count();
            System.out.println("Current project types count: " + count);
            
            if (count == 0) {
                System.out.println("Initializing default project types...");
                // Create default project types
                ProjectType[] defaultTypes = {
                    ProjectType.builder().typeName("Infrastructure").typeCode("INFRA").typeDescription("Infrastructure development projects").isActive(true).build(),
                    ProjectType.builder().typeName("Education").typeCode("EDU").typeDescription("Educational improvement projects").isActive(true).build(),
                    ProjectType.builder().typeName("Technology").typeCode("TECH").typeDescription("Technology enhancement projects").isActive(true).build(),
                    ProjectType.builder().typeName("Health & Safety").typeCode("HEALTH").typeDescription("Health and safety projects").isActive(true).build(),
                    ProjectType.builder().typeName("Sports & Recreation").typeCode("SPORTS").typeDescription("Sports and recreation projects").isActive(true).build(),
                    ProjectType.builder().typeName("Arts & Culture").typeCode("ARTS").typeDescription("Arts and culture projects").isActive(true).build(),
                    ProjectType.builder().typeName("Environment").typeCode("ENV").typeDescription("Environmental projects").isActive(true).build()
                };
                
                for (ProjectType type : defaultTypes) {
                    ProjectType saved = projectTypeRepository.save(type);
                    System.out.println("Created project type: " + saved.getTypeName() + " with ID: " + saved.getProjectTypeId());
                }
                System.out.println("Successfully initialized " + defaultTypes.length + " project types");
            } else {
                System.out.println("Project types already exist, skipping initialization");
            }
        } catch (Exception e) {
            System.err.println("Error initializing project types: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Create or update SchoolProject
    public SchoolProjectDto saveSchoolProject(SchoolProjectDto schoolProjectDto) {
        School school = schoolRepository.findById(schoolProjectDto.getSchoolId())
                .orElseThrow(() -> new RuntimeException("School not found"));

            // Ensure project types are initialized
        initializeProjectTypes();
        
        ProjectType projectType = projectTypeRepository.findById(schoolProjectDto.getProjectTypeId())
                .orElseThrow(() -> new RuntimeException("Project type not found with ID: " + schoolProjectDto.getProjectTypeId()));

        SchoolProject schoolProject = SchoolProject.builder()
                .projectId(schoolProjectDto.getProjectId())
                .school(school)
                .projectTitle(schoolProjectDto.getProjectTitle())
                .projectDescription(schoolProjectDto.getProjectDescription())
                .projectType(projectType)
                .requiredAmount(schoolProjectDto.getRequiredAmount())
                .build();

        SchoolProject saved = schoolProjectRepository.save(schoolProject);
        return mapToDto(saved);
    }

    // Get all school projects
    public List<SchoolProjectDto> getAllSchoolProjects() {
        return schoolProjectRepository.findAllWithProjectType().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Get school project by ID
    public SchoolProjectDto getSchoolProjectById(Integer projectId) {
        SchoolProject project = schoolProjectRepository.findByIdWithProjectType(projectId);
        if (project == null) {
            throw new RuntimeException("School project not found");
        }
        return mapToDto(project);
    }

    // Delete school project
    public void deleteSchoolProject(Integer projectId) {
        if (!schoolProjectRepository.existsById(projectId)) {
            throw new RuntimeException("School project not found");
        }
        schoolProjectRepository.deleteById(projectId);
    }
    
    // Get all project types for dropdown
    public List<ProjectType> getAllProjectTypes() {
        initializeProjectTypes();
        return projectTypeRepository.findAll();
    }
    
    // Get project completion rate
    public Integer getProjectCompletionRate(Integer projectId) {
        Double completionRate = schoolProjectRepository.getLatestCompletionRate(projectId);
        return completionRate != null ? completionRate.intValue() : 0;
    }

    // Map SchoolProject entity to DTO
    private SchoolProjectDto mapToDto(SchoolProject schoolProject) {
        java.math.BigDecimal raisedAmount = schoolProjectRepository.getTotalRaisedAmount(schoolProject.getProjectId());
        Double completionRate = schoolProjectRepository.getLatestCompletionRate(schoolProject.getProjectId());
        
        return SchoolProjectDto.builder()
                .projectId(schoolProject.getProjectId())
                .schoolId(schoolProject.getSchool().getSchoolId())
                .projectTitle(schoolProject.getProjectTitle())
                .projectDescription(schoolProject.getProjectDescription())
                .projectTypeId(schoolProject.getProjectType().getProjectTypeId())
                .projectTypeName(schoolProject.getProjectType().getTypeName())
                .requiredAmount(schoolProject.getRequiredAmount())
                .raisedAmount(raisedAmount != null ? raisedAmount : java.math.BigDecimal.ZERO)
                .completionRate(completionRate != null ? completionRate : 0.0)
                .createdAt(schoolProject.getCreatedAt())
                .updatedAt(schoolProject.getUpdatedAt())
                .build();
    }
}