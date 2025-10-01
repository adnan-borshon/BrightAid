package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Entity.ProjectUpdate;
import com.example.Bright_Aid.Entity.SchoolProject;
import com.example.Bright_Aid.repository.ProjectUpdateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectUpdateService {

    private final ProjectUpdateRepository projectUpdateRepository;

    public List<ProjectUpdate> getAllUpdates() {
        return projectUpdateRepository.findAll();
    }

    public ProjectUpdate getUpdateById(Integer id) {
        return projectUpdateRepository.findById(id).orElse(null);
    }

    public ProjectUpdate createUpdate(ProjectUpdate update, Integer projectId) {
        // Set project relationship using projectId
        SchoolProject project = new SchoolProject();
        project.setProjectId(projectId);
        update.setProject(project);
        
        return projectUpdateRepository.save(update);
    }

    public ProjectUpdate updateUpdate(Integer id, ProjectUpdate update) {
        update.setUpdateId(id);
        return projectUpdateRepository.save(update);
    }

    public void deleteUpdate(Integer id) {
        projectUpdateRepository.deleteById(id);
    }
}
