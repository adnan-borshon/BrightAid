package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.NgoProjectDto;
import com.example.Bright_Aid.Entity.NgoProject;
import com.example.Bright_Aid.service.NgoProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ngo-projects")
@RequiredArgsConstructor
public class NgoProjectController {

    private final NgoProjectService ngoProjectService;

    // GET all projects
    @GetMapping
    public ResponseEntity<List<NgoProjectDto>> getAllProjects() {
        List<NgoProjectDto> dtos = ngoProjectService.getAllProjects()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // GET project by ID
    @GetMapping("/{id}")
    public ResponseEntity<NgoProjectDto> getProjectById(@PathVariable Integer id) {
        NgoProject project = ngoProjectService.getProjectById(id);
        return ResponseEntity.ok(toDto(project));
    }

    // POST create project
    @PostMapping
    public ResponseEntity<NgoProjectDto> createProject(@RequestBody NgoProjectDto dto) {
        NgoProject project = fromDto(dto);
        NgoProject saved = ngoProjectService.createProject(project, dto.getNgoId(), dto.getProjectTypeId());
        return ResponseEntity.ok(toDto(saved));
    }

    // PUT update project
    @PutMapping("/{id}")
    public ResponseEntity<NgoProjectDto> updateProject(@PathVariable Integer id,
                                                       @RequestBody NgoProjectDto dto) {
        NgoProject project = fromDto(dto);
        NgoProject saved = ngoProjectService.updateProject(id, project);
        return ResponseEntity.ok(toDto(saved));
    }

    // DELETE project
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Integer id) {
        ngoProjectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    // --- Helper methods ---
    private NgoProjectDto toDto(NgoProject project) {
        if (project == null) return null;
        return NgoProjectDto.builder()
                .ngoProjectId(project.getNgoProjectId())
                .ngoId(project.getNgo() != null ? project.getNgo().getNgoId() : null)
                .projectName(project.getProjectName())
                .projectDescription(project.getProjectDescription())
                .projectTypeId(project.getProjectType() != null ? project.getProjectType().getProjectTypeId() : null)
                .budget(project.getBudget())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .status(project.getStatus().name())
                .build();
    }

    private NgoProject fromDto(NgoProjectDto dto) {
        NgoProject project = new NgoProject();
        project.setProjectName(dto.getProjectName());
        project.setProjectDescription(dto.getProjectDescription());
        project.setBudget(dto.getBudget());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        if (dto.getStatus() != null) {
            try {
                project.setStatus(NgoProject.ProjectStatus.valueOf(dto.getStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid project status: " + dto.getStatus() + ". Valid values are: PLANNED, ACTIVE, COMPLETED, CANCELLED");
            }
        }
        return project;
    }
}
