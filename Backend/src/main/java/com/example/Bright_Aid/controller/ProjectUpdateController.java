package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.ProjectUpdateDto;
import com.example.Bright_Aid.Entity.ProjectUpdate;
import com.example.Bright_Aid.service.ProjectUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/project-updates")
@RequiredArgsConstructor
public class ProjectUpdateController {

    private final ProjectUpdateService projectUpdateService;

    // GET all updates
    @GetMapping
    public ResponseEntity<List<ProjectUpdateDto>> getAllUpdates() {
        List<ProjectUpdateDto> dtos = projectUpdateService.getAllUpdates()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // GET update by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProjectUpdateDto> getUpdateById(@PathVariable Integer id) {
        ProjectUpdate update = projectUpdateService.getUpdateById(id);
        return ResponseEntity.ok(toDto(update));
    }

    // POST create new update
    @PostMapping
    public ResponseEntity<ProjectUpdateDto> createUpdate(@RequestBody ProjectUpdateDto dto) {
        ProjectUpdate update = fromDto(dto);
        ProjectUpdate saved = projectUpdateService.createUpdate(update, dto.getProjectId());
        return ResponseEntity.ok(toDto(saved));
    }

    // PUT update existing
    @PutMapping("/{id}")
    public ResponseEntity<ProjectUpdateDto> updateUpdate(@PathVariable Integer id,
                                                         @RequestBody ProjectUpdateDto dto) {
        ProjectUpdate update = fromDto(dto);
        ProjectUpdate saved = projectUpdateService.updateUpdate(id, update);
        return ResponseEntity.ok(toDto(saved));
    }

    // DELETE update
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUpdate(@PathVariable Integer id) {
        projectUpdateService.deleteUpdate(id);
        return ResponseEntity.noContent().build();
    }

    // Helper methods to convert
    private ProjectUpdateDto toDto(ProjectUpdate update) {
        return ProjectUpdateDto.builder()
                .updateId(update.getUpdateId())
                .projectId(update.getProject().getProjectId()) // Correct getter
                .updateTitle(update.getUpdateTitle())
                .updateDescription(update.getUpdateDescription())
                .progressPercentage(update.getProgressPercentage())
                .amountUtilized(update.getAmountUtilized())
                .imagesUrls(update.getImagesUrls())
                .build();
    }

    private ProjectUpdate fromDto(ProjectUpdateDto dto) {
        return ProjectUpdate.builder()
                .updateTitle(dto.getUpdateTitle())
                .updateDescription(dto.getUpdateDescription())
                .progressPercentage(dto.getProgressPercentage())
                .amountUtilized(dto.getAmountUtilized())
                .imagesUrls(dto.getImagesUrls())
                .build();
    }
}
