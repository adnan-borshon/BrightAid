package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Dto.NgoProjectDto;
import com.example.Bright_Aid.Entity.NgoProject;
import com.example.Bright_Aid.Entity.Ngo;
import com.example.Bright_Aid.Entity.ProjectType;
import com.example.Bright_Aid.Entity.NgoProjectSchool;
import com.example.Bright_Aid.repository.NgoProjectRepository;
import com.example.Bright_Aid.repository.NgoRepository;
import com.example.Bright_Aid.repository.ProjectTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class NgoProjectService {

    private final NgoProjectRepository ngoProjectRepository;
    private final NgoRepository ngoRepository;
    private final ProjectTypeRepository projectTypeRepository;

    @Transactional
    public NgoProjectDto createNgoProject(NgoProjectDto ngoProjectDto) {
        log.info("Creating NGO project for NGO ID: {}", ngoProjectDto.getNgoId());

        ProjectType projectType = projectTypeRepository.findById(ngoProjectDto.getProjectTypeId())
                .orElseThrow(() -> new RuntimeException("Project type not found with ID: " + ngoProjectDto.getProjectTypeId()));

        NgoProject.NgoProjectBuilder projectBuilder = NgoProject.builder()
                .projectType(projectType)
                .budget(ngoProjectDto.getBudget())
                .startDate(ngoProjectDto.getStartDate())
                .endDate(ngoProjectDto.getEndDate())
                .status(ngoProjectDto.getStatus());

        // Set NGO if provided
        if (ngoProjectDto.getNgoId() != null) {
            Ngo ngo = ngoRepository.findById(ngoProjectDto.getNgoId())
                    .orElseThrow(() -> new RuntimeException("NGO not found with ID: " + ngoProjectDto.getNgoId()));
            projectBuilder.ngo(ngo);
        }

        NgoProject ngoProject = projectBuilder.build();
        NgoProject savedProject = ngoProjectRepository.save(ngoProject);

        log.info("Successfully created NGO project with ID: {}", savedProject.getNgoProjectId());
        return convertToDto(savedProject);
    }

    public NgoProjectDto getNgoProjectById(Integer ngoProjectId) {
        log.info("Fetching NGO project with ID: {}", ngoProjectId);

        NgoProject project = ngoProjectRepository.findById(ngoProjectId)
                .orElseThrow(() -> new RuntimeException("NGO project not found with ID: " + ngoProjectId));

        return convertToDto(project);
    }

    public List<NgoProjectDto> getAllNgoProjects() {
        log.info("Fetching all NGO projects");

        List<NgoProject> projects = ngoProjectRepository.findAll();
        return projects.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<NgoProjectDto> getAllNgoProjects(Pageable pageable) {
        log.info("Fetching NGO projects with pagination: {}", pageable);

        Page<NgoProject> projects = ngoProjectRepository.findAll(pageable);
        return projects.map(this::convertToDto);
    }

    @Transactional
    public NgoProjectDto updateNgoProject(Integer ngoProjectId, NgoProjectDto ngoProjectDto) {
        log.info("Updating NGO project with ID: {}", ngoProjectId);

        NgoProject existingProject = ngoProjectRepository.findById(ngoProjectId)
                .orElseThrow(() -> new RuntimeException("NGO project not found with ID: " + ngoProjectId));

        // Update NGO if provided
        if (ngoProjectDto.getNgoId() != null &&
                (existingProject.getNgo() == null || !ngoProjectDto.getNgoId().equals(existingProject.getNgo().getNgoId()))) {
            Ngo ngo = ngoRepository.findById(ngoProjectDto.getNgoId())
                    .orElseThrow(() -> new RuntimeException("NGO not found with ID: " + ngoProjectDto.getNgoId()));
            existingProject.setNgo(ngo);
        }

        // Update Project Type if provided
        if (ngoProjectDto.getProjectTypeId() != null &&
                !ngoProjectDto.getProjectTypeId().equals(existingProject.getProjectType().getProjectTypeId())) {
            ProjectType projectType = projectTypeRepository.findById(ngoProjectDto.getProjectTypeId())
                    .orElseThrow(() -> new RuntimeException("Project type not found with ID: " + ngoProjectDto.getProjectTypeId()));
            existingProject.setProjectType(projectType);
        }

        if (ngoProjectDto.getBudget() != null) {
            existingProject.setBudget(ngoProjectDto.getBudget());
        }
        if (ngoProjectDto.getStartDate() != null) {
            existingProject.setStartDate(ngoProjectDto.getStartDate());
        }
        if (ngoProjectDto.getEndDate() != null) {
            existingProject.setEndDate(ngoProjectDto.getEndDate());
        }
        if (ngoProjectDto.getStatus() != null) {
            existingProject.setStatus(ngoProjectDto.getStatus());
        }

        NgoProject updatedProject = ngoProjectRepository.save(existingProject);
        log.info("Successfully updated NGO project with ID: {}", ngoProjectId);

        return convertToDto(updatedProject);
    }

    @Transactional
    public void deleteNgoProject(Integer ngoProjectId) {
        log.info("Deleting NGO project with ID: {}", ngoProjectId);

        if (!ngoProjectRepository.existsById(ngoProjectId)) {
            throw new RuntimeException("NGO project not found with ID: " + ngoProjectId);
        }

        ngoProjectRepository.deleteById(ngoProjectId);
        log.info("Successfully deleted NGO project with ID: {}", ngoProjectId);
    }

    public List<NgoProjectDto> getNgoProjectsByStatus(NgoProject.ProjectStatus status) {
        log.info("Fetching NGO projects with status: {}", status);

        List<NgoProject> projects = ngoProjectRepository.findAll();
        return projects.stream()
                .filter(project -> project.getStatus().equals(status))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<NgoProjectDto> getNgoProjectsByNgo(Integer ngoId) {
        log.info("Fetching NGO projects for NGO ID: {}", ngoId);

        List<NgoProject> projects = ngoProjectRepository.findAll();
        return projects.stream()
                .filter(project -> project.getNgo() != null && project.getNgo().getNgoId().equals(ngoId))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<NgoProjectDto> getNgoProjectsByProjectType(Integer projectTypeId) {
        log.info("Fetching NGO projects for project type ID: {}", projectTypeId);

        List<NgoProject> projects = ngoProjectRepository.findAll();
        return projects.stream()
                .filter(project -> project.getProjectType().getProjectTypeId().equals(projectTypeId))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<NgoProjectDto> getActiveNgoProjects() {
        log.info("Fetching active NGO projects");

        List<NgoProject> projects = ngoProjectRepository.findAll();
        return projects.stream()
                .filter(project -> project.getStatus().equals(NgoProject.ProjectStatus.ACTIVE))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<NgoProjectDto> getCompletedNgoProjects() {
        log.info("Fetching completed NGO projects");

        List<NgoProject> projects = ngoProjectRepository.findAll();
        return projects.stream()
                .filter(project -> project.getStatus().equals(NgoProject.ProjectStatus.COMPLETED))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<NgoProjectDto> getNgoProjectsByDateRange(LocalDate startDate, LocalDate endDate) {
        log.info("Fetching NGO projects between {} and {}", startDate, endDate);

        List<NgoProject> projects = ngoProjectRepository.findAll();
        return projects.stream()
                .filter(project -> {
                    LocalDate projectStart = project.getStartDate();
                    LocalDate projectEnd = project.getEndDate();
                    return projectStart != null && projectEnd != null &&
                            !projectStart.isBefore(startDate) && !projectEnd.isAfter(endDate);
                })
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public NgoProjectDto updateProjectStatus(Integer ngoProjectId, NgoProject.ProjectStatus status) {
        log.info("Updating project status for ID: {} to status: {}", ngoProjectId, status);

        NgoProject project = ngoProjectRepository.findById(ngoProjectId)
                .orElseThrow(() -> new RuntimeException("NGO project not found with ID: " + ngoProjectId));

        project.setStatus(status);
        NgoProject updatedProject = ngoProjectRepository.save(project);

        log.info("Successfully updated project status for ID: {}", ngoProjectId);
        return convertToDto(updatedProject);
    }

    private NgoProjectDto convertToDto(NgoProject project) {
        List<Integer> schoolParticipationIds = null;
        if (project.getSchoolParticipations() != null) {
            schoolParticipationIds = project.getSchoolParticipations().stream()
                    .map(NgoProjectSchool::getNpsId)
                    .collect(Collectors.toList());
        }

        return NgoProjectDto.builder()
                .ngoProjectId(project.getNgoProjectId())
                .ngoId(project.getNgo() != null ? project.getNgo().getNgoId() : null)
                .projectTypeId(project.getProjectType().getProjectTypeId())
                .budget(project.getBudget())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .status(project.getStatus())
                .schoolParticipationIds(schoolParticipationIds)
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}