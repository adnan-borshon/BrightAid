package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Dto.ProjectUpdateDto;
import com.example.Bright_Aid.Entity.ProjectUpdate;
import com.example.Bright_Aid.Entity.SchoolProject;
import com.example.Bright_Aid.Entity.User;
import com.example.Bright_Aid.repository.ProjectUpdateRepository;
import com.example.Bright_Aid.repository.SchoolProjectRepository;
import com.example.Bright_Aid.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProjectUpdateService {

    private final ProjectUpdateRepository projectUpdateRepository;
    private final SchoolProjectRepository schoolProjectRepository;
    private final UserRepository userRepository;

    @Transactional
    public ProjectUpdateDto createProjectUpdate(ProjectUpdateDto projectUpdateDto) {
        log.info("Creating project update for project ID: {}", projectUpdateDto.getProjectId());

        SchoolProject project = schoolProjectRepository.findById(projectUpdateDto.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectUpdateDto.getProjectId()));

        User user = userRepository.findById(projectUpdateDto.getUpdatedBy())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + projectUpdateDto.getUpdatedBy()));

        ProjectUpdate projectUpdate = ProjectUpdate.builder()
                .project(project)
                .updatedBy(user)
                .updateTitle(projectUpdateDto.getUpdateTitle())
                .updateDescription(projectUpdateDto.getUpdateDescription())
                .progressPercentage(projectUpdateDto.getProgressPercentage())
                .amountUtilized(projectUpdateDto.getAmountUtilized())
                .imagesUrls(projectUpdateDto.getImagesUrls())
                .build();

        ProjectUpdate savedProjectUpdate = projectUpdateRepository.save(projectUpdate);
        log.info("Successfully created project update with ID: {}", savedProjectUpdate.getUpdateId());

        return convertToDto(savedProjectUpdate);
    }

    public ProjectUpdateDto getProjectUpdateById(Integer updateId) {
        log.info("Fetching project update with ID: {}", updateId);

        ProjectUpdate projectUpdate = projectUpdateRepository.findById(updateId)
                .orElseThrow(() -> new RuntimeException("Project update not found with ID: " + updateId));

        return convertToDto(projectUpdate);
    }

    public List<ProjectUpdateDto> getAllProjectUpdates() {
        log.info("Fetching all project updates");

        List<ProjectUpdate> projectUpdates = projectUpdateRepository.findAll();
        return projectUpdates.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<ProjectUpdateDto> getAllProjectUpdates(Pageable pageable) {
        log.info("Fetching project updates with pagination: {}", pageable);

        Page<ProjectUpdate> projectUpdates = projectUpdateRepository.findAll(pageable);
        return projectUpdates.map(this::convertToDto);
    }

    @Transactional
    public ProjectUpdateDto updateProjectUpdate(Integer updateId, ProjectUpdateDto projectUpdateDto) {
        log.info("Updating project update with ID: {}", updateId);

        ProjectUpdate existingUpdate = projectUpdateRepository.findById(updateId)
                .orElseThrow(() -> new RuntimeException("Project update not found with ID: " + updateId));

        if (projectUpdateDto.getProjectId() != null) {
            SchoolProject project = schoolProjectRepository.findById(projectUpdateDto.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectUpdateDto.getProjectId()));
            existingUpdate.setProject(project);
        }

        if (projectUpdateDto.getUpdatedBy() != null) {
            User user = userRepository.findById(projectUpdateDto.getUpdatedBy())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + projectUpdateDto.getUpdatedBy()));
            existingUpdate.setUpdatedBy(user);
        }

        if (projectUpdateDto.getUpdateTitle() != null) {
            existingUpdate.setUpdateTitle(projectUpdateDto.getUpdateTitle());
        }
        if (projectUpdateDto.getUpdateDescription() != null) {
            existingUpdate.setUpdateDescription(projectUpdateDto.getUpdateDescription());
        }
        if (projectUpdateDto.getProgressPercentage() != null) {
            existingUpdate.setProgressPercentage(projectUpdateDto.getProgressPercentage());
        }
        if (projectUpdateDto.getAmountUtilized() != null) {
            existingUpdate.setAmountUtilized(projectUpdateDto.getAmountUtilized());
        }
        if (projectUpdateDto.getImagesUrls() != null) {
            existingUpdate.setImagesUrls(projectUpdateDto.getImagesUrls());
        }

        ProjectUpdate updatedProjectUpdate = projectUpdateRepository.save(existingUpdate);
        log.info("Successfully updated project update with ID: {}", updateId);

        return convertToDto(updatedProjectUpdate);
    }

    @Transactional
    public void deleteProjectUpdate(Integer updateId) {
        log.info("Deleting project update with ID: {}", updateId);

        ProjectUpdate projectUpdate = projectUpdateRepository.findById(updateId)
                .orElseThrow(() -> new RuntimeException("Project update not found with ID: " + updateId));

        projectUpdateRepository.deleteById(updateId);
        log.info("Successfully deleted project update with ID: {}", updateId);
    }

    private ProjectUpdateDto convertToDto(ProjectUpdate projectUpdate) {
        return ProjectUpdateDto.builder()
                .updateId(projectUpdate.getUpdateId())
                .projectId(projectUpdate.getProject().getProjectId()) // Assuming getId() method exists
                .updatedBy(projectUpdate.getUpdatedBy().getUserId()) // Assuming getId() method exists
                .updateTitle(projectUpdate.getUpdateTitle())
                .updateDescription(projectUpdate.getUpdateDescription())
                .progressPercentage(projectUpdate.getProgressPercentage())
                .amountUtilized(projectUpdate.getAmountUtilized())
                .imagesUrls(projectUpdate.getImagesUrls())
                .build();
    }
}