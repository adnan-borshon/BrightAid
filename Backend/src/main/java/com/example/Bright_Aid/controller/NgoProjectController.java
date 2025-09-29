package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.NgoProjectDto;
import com.example.Bright_Aid.Entity.NgoProject;
import com.example.Bright_Aid.service.NgoProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/ngo-projects")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class NgoProjectController {

    private final NgoProjectService ngoProjectService;

    @PostMapping
    public ResponseEntity<NgoProjectDto> createNgoProject(
            @Valid @RequestBody NgoProjectDto ngoProjectDto) {
        log.info("REST request to create NGO project for NGO ID: {}", ngoProjectDto.getNgoId());

        NgoProjectDto createdProject = ngoProjectService.createNgoProject(ngoProjectDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

    @GetMapping("/{ngoProjectId}")
    public ResponseEntity<NgoProjectDto> getNgoProjectById(@PathVariable Integer ngoProjectId) {
        log.info("REST request to get NGO project with ID: {}", ngoProjectId);

        NgoProjectDto project = ngoProjectService.getNgoProjectById(ngoProjectId);
        return ResponseEntity.ok(project);
    }

    @GetMapping
    public ResponseEntity<List<NgoProjectDto>> getAllNgoProjects() {
        log.info("REST request to get all NGO projects");

        List<NgoProjectDto> projects = ngoProjectService.getAllNgoProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<NgoProjectDto>> getAllNgoProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ngoProjectId") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        log.info("REST request to get NGO projects with pagination - page: {}, size: {}, sortBy: {}, sortDirection: {}",
                page, size, sortBy, sortDirection);

        Sort sort = sortDirection.equalsIgnoreCase("DESC") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<NgoProjectDto> projects = ngoProjectService.getAllNgoProjects(pageable);

        return ResponseEntity.ok(projects);
    }

    @GetMapping("/by-status/{status}")
    public ResponseEntity<List<NgoProjectDto>> getNgoProjectsByStatus(
            @PathVariable NgoProject.ProjectStatus status) {
        log.info("REST request to get NGO projects with status: {}", status);

        List<NgoProjectDto> projects = ngoProjectService.getNgoProjectsByStatus(status);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/by-ngo/{ngoId}")
    public ResponseEntity<List<NgoProjectDto>> getNgoProjectsByNgo(@PathVariable Integer ngoId) {
        log.info("REST request to get NGO projects for NGO ID: {}", ngoId);

        List<NgoProjectDto> projects = ngoProjectService.getNgoProjectsByNgo(ngoId);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/by-project-type/{projectTypeId}")
    public ResponseEntity<List<NgoProjectDto>> getNgoProjectsByProjectType(@PathVariable Integer projectTypeId) {
        log.info("REST request to get NGO projects for project type ID: {}", projectTypeId);

        List<NgoProjectDto> projects = ngoProjectService.getNgoProjectsByProjectType(projectTypeId);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/active")
    public ResponseEntity<List<NgoProjectDto>> getActiveNgoProjects() {
        log.info("REST request to get active NGO projects");

        List<NgoProjectDto> projects = ngoProjectService.getActiveNgoProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/completed")
    public ResponseEntity<List<NgoProjectDto>> getCompletedNgoProjects() {
        log.info("REST request to get completed NGO projects");

        List<NgoProjectDto> projects = ngoProjectService.getCompletedNgoProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/by-date-range")
    public ResponseEntity<List<NgoProjectDto>> getNgoProjectsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("REST request to get NGO projects between {} and {}", startDate, endDate);

        List<NgoProjectDto> projects = ngoProjectService.getNgoProjectsByDateRange(startDate, endDate);
        return ResponseEntity.ok(projects);
    }

    @PutMapping("/{ngoProjectId}")
    public ResponseEntity<NgoProjectDto> updateNgoProject(
            @PathVariable Integer ngoProjectId,
            @Valid @RequestBody NgoProjectDto ngoProjectDto) {
        log.info("REST request to update NGO project with ID: {}", ngoProjectId);

        NgoProjectDto updatedProject = ngoProjectService.updateNgoProject(ngoProjectId, ngoProjectDto);
        return ResponseEntity.ok(updatedProject);
    }

    @PatchMapping("/{ngoProjectId}/status")
    public ResponseEntity<NgoProjectDto> updateProjectStatus(
            @PathVariable Integer ngoProjectId,
            @RequestParam NgoProject.ProjectStatus status) {
        log.info("REST request to update status for NGO project ID: {} to status: {}", ngoProjectId, status);

        NgoProjectDto updatedProject = ngoProjectService.updateProjectStatus(ngoProjectId, status);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{ngoProjectId}")
    public ResponseEntity<Void> deleteNgoProject(@PathVariable Integer ngoProjectId) {
        log.info("REST request to delete NGO project with ID: {}", ngoProjectId);

        ngoProjectService.deleteNgoProject(ngoProjectId);
        return ResponseEntity.noContent().build();
    }
}