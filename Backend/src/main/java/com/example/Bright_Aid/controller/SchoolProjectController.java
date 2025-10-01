package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.SchoolProjectDto;
import com.example.Bright_Aid.service.SchoolProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/school-projects")
@CrossOrigin(origins = "*")
public class SchoolProjectController {

    private final SchoolProjectService schoolProjectService;

    public SchoolProjectController(SchoolProjectService schoolProjectService) {
        this.schoolProjectService = schoolProjectService;
    }

    // Create new school project
    @PostMapping
    public ResponseEntity<SchoolProjectDto> createSchoolProject(@Valid @RequestBody SchoolProjectDto schoolProjectDto) {
        SchoolProjectDto createdProject = schoolProjectService.saveSchoolProject(schoolProjectDto);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    // Get all school projects
    @GetMapping
    public ResponseEntity<List<SchoolProjectDto>> getAllSchoolProjects() {
        List<SchoolProjectDto> projects = schoolProjectService.getAllSchoolProjects();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    // Get school project by ID
    @GetMapping("/{projectId}")
    public ResponseEntity<SchoolProjectDto> getSchoolProjectById(@PathVariable Integer projectId) {
        SchoolProjectDto project = schoolProjectService.getSchoolProjectById(projectId);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    // Update school project
    @PutMapping("/{projectId}")
    public ResponseEntity<SchoolProjectDto> updateSchoolProject(@PathVariable Integer projectId,
                                                                @Valid @RequestBody SchoolProjectDto schoolProjectDto) {
        schoolProjectDto.setProjectId(projectId);
        SchoolProjectDto updatedProject = schoolProjectService.saveSchoolProject(schoolProjectDto);
        return new ResponseEntity<>(updatedProject, HttpStatus.OK);
    }

    // Delete school project
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteSchoolProject(@PathVariable Integer projectId) {
        schoolProjectService.deleteSchoolProject(projectId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    // Get all project types
    @GetMapping("/types")
    public ResponseEntity<List<com.example.Bright_Aid.Entity.ProjectType>> getProjectTypes() {
        List<com.example.Bright_Aid.Entity.ProjectType> projectTypes = schoolProjectService.getAllProjectTypes();
        return new ResponseEntity<>(projectTypes, HttpStatus.OK);
    }
    
    // Initialize project types (for testing)
    @PostMapping("/init-types")
    public ResponseEntity<String> initializeProjectTypes() {
        schoolProjectService.initializeProjectTypes();
        return new ResponseEntity<>("Project types initialized", HttpStatus.OK);
    }
    
    // Get project completion rate
    @GetMapping("/{projectId}/completion-rate")
    public ResponseEntity<Integer> getProjectCompletionRate(@PathVariable Integer projectId) {
        Integer completionRate = schoolProjectService.getProjectCompletionRate(projectId);
        return new ResponseEntity<>(completionRate, HttpStatus.OK);
    }

}