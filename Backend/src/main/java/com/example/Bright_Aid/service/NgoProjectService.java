package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Entity.NgoProject;
import com.example.Bright_Aid.Entity.Ngo;
import com.example.Bright_Aid.Entity.ProjectType;
import com.example.Bright_Aid.repository.NgoProjectRepository;
import com.example.Bright_Aid.repository.NgoRepository;
import com.example.Bright_Aid.repository.ProjectTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NgoProjectService {

    private final NgoProjectRepository ngoProjectRepository;
    private final NgoRepository ngoRepository;
    private final ProjectTypeRepository projectTypeRepository;

    // Get all projects
    public List<NgoProject> getAllProjects() {
        return ngoProjectRepository.findAll();
    }

    // Get project by ID
    public NgoProject getProjectById(Integer id) {
        return ngoProjectRepository.findById(id).orElse(null);
    }

    // Create project
    public NgoProject createProject(NgoProject project, Integer ngoId, Integer projectTypeId) {
        if (ngoId != null) {
            Ngo ngo = ngoRepository.findById(ngoId)
                    .orElseThrow(() -> new RuntimeException("NGO not found with ID: " + ngoId));
            project.setNgo(ngo);
        }
        
        if (projectTypeId != null) {
            ProjectType projectType = projectTypeRepository.findById(projectTypeId)
                    .orElseThrow(() -> new RuntimeException("Project Type not found with ID: " + projectTypeId));
            project.setProjectType(projectType);
        } else {
            throw new RuntimeException("Project Type ID is required");
        }
        
        return ngoProjectRepository.save(project);
    }

    // Update project
    public NgoProject updateProject(Integer id, NgoProject project) {
        project.setNgoProjectId(id);
        return ngoProjectRepository.save(project);
    }

    // Delete project
    public void deleteProject(Integer id) {
        ngoProjectRepository.deleteById(id);
    }
}
