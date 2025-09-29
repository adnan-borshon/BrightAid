package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.ProjectUpdateDto;
import com.example.Bright_Aid.service.ProjectUpdateService;
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
@RequestMapping("/api/project-updates")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ProjectUpdateController {

    private final ProjectUpdateService projectUpdateService;

    @PostMapping
    public ResponseEntity<ProjectUpdateDto> createProjectUpdate(@Valid @RequestBody ProjectUpdateDto projectUpdateDto) {
        log.info("Creating new project update for project ID: {}", projectUpdateDto.getProjectId());

        ProjectUpdateDto createdProjectUpdate = projectUpdateService.createProjectUpdate(projectUpdateDto);

        log.info("Successfully created project update with ID: {}", createdProjectUpdate.getUpdateId());
        return new ResponseEntity<>(createdProjectUpdate, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectUpdateDto> getProjectUpdateById(@PathVariable Integer id) {
        log.info("Fetching project update with ID: {}", id);

        ProjectUpdateDto projectUpdate = projectUpdateService.getProjectUpdateById(id);

        log.info("Successfully retrieved project update with ID: {}", id);
        return ResponseEntity.ok(projectUpdate);
    }

    @GetMapping
    public ResponseEntity<List<ProjectUpdateDto>> getAllProjectUpdates() {
        log.info("Fetching all project updates");

        List<ProjectUpdateDto> projectUpdates = projectUpdateService.getAllProjectUpdates();

        log.info("Successfully retrieved {} project updates", projectUpdates.size());
        return ResponseEntity.ok(projectUpdates);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<ProjectUpdateDto>> getAllProjectUpdatesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "updateId") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        log.info("Fetching paginated project updates - Page: {}, Size: {}, Sort: {} {}", page, size, sortBy, sortDir);

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ProjectUpdateDto> projectUpdates = projectUpdateService.getAllProjectUpdates(pageable);

        log.info("Successfully retrieved {} project updates out of {} total",
                projectUpdates.getNumberOfElements(), projectUpdates.getTotalElements());
        return ResponseEntity.ok(projectUpdates);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectUpdateDto> updateProjectUpdate(@PathVariable Integer id,
                                                                @Valid @RequestBody ProjectUpdateDto projectUpdateDto) {
        log.info("Updating project update with ID: {}", id);

        ProjectUpdateDto updatedProjectUpdate = projectUpdateService.updateProjectUpdate(id, projectUpdateDto);

        log.info("Successfully updated project update with ID: {}", id);
        return ResponseEntity.ok(updatedProjectUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjectUpdate(@PathVariable Integer id) {
        log.info("Deleting project update with ID: {}", id);

        projectUpdateService.deleteProjectUpdate(id);

        log.info("Successfully deleted project update with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}