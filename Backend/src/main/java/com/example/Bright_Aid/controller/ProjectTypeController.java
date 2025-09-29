package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.ProjectTypeDto;
import com.example.Bright_Aid.service.ProjectTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project-types")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ProjectTypeController {

    private final ProjectTypeService projectTypeService;

    @PostMapping
    public ResponseEntity<ProjectTypeDto> createProjectType(@Valid @RequestBody ProjectTypeDto projectTypeDto) {
        log.info("Creating new project type with name: {}", projectTypeDto.getTypeName());

        ProjectTypeDto createdProjectType = projectTypeService.createProjectType(projectTypeDto);

        log.info("Successfully created project type with ID: {}", createdProjectType.getProjectTypeId());
        return new ResponseEntity<>(createdProjectType, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectTypeDto> getProjectTypeById(@PathVariable Integer id) {
        log.info("Fetching project type with ID: {}", id);

        ProjectTypeDto projectType = projectTypeService.getProjectTypeById(id);

        log.info("Successfully retrieved project type with ID: {}", id);
        return ResponseEntity.ok(projectType);
    }

    @GetMapping
    public ResponseEntity<List<ProjectTypeDto>> getAllProjectTypes() {
        log.info("Fetching all project types");

        List<ProjectTypeDto> projectTypes = projectTypeService.getAllProjectTypes();

        log.info("Successfully retrieved {} project types", projectTypes.size());
        return ResponseEntity.ok(projectTypes);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<ProjectTypeDto>> getAllProjectTypesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "projectTypeId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        log.info("Fetching paginated project types - Page: {}, Size: {}, Sort: {} {}", page, size, sortBy, sortDir);

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ProjectTypeDto> projectTypes = projectTypeService.getAllProjectTypes(pageable);

        log.info("Successfully retrieved {} project types out of {} total",
                projectTypes.getNumberOfElements(), projectTypes.getTotalElements());
        return ResponseEntity.ok(projectTypes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectTypeDto> updateProjectType(@PathVariable Integer id,
                                                            @Valid @RequestBody ProjectTypeDto projectTypeDto) {
        log.info("Updating project type with ID: {}", id);

        ProjectTypeDto updatedProjectType = projectTypeService.updateProjectType(id, projectTypeDto);

        log.info("Successfully updated project type with ID: {}", id);
        return ResponseEntity.ok(updatedProjectType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjectType(@PathVariable Integer id) {
        log.info("Deleting project type with ID: {}", id);

        projectTypeService.deleteProjectType(id);

        log.info("Successfully deleted project type with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}