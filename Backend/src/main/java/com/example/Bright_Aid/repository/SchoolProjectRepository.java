package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.SchoolProject;
import com.example.Bright_Aid.Entity.School;
import com.example.Bright_Aid.Entity.User;
import com.example.Bright_Aid.Entity.ProjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolProjectRepository extends JpaRepository<SchoolProject, Integer> {

    @Query("SELECT sp FROM SchoolProject sp JOIN FETCH sp.projectType WHERE sp.projectId = :projectId")
    SchoolProject findByIdWithProjectType(@Param("projectId") Integer projectId);
    
    @Query("SELECT sp FROM SchoolProject sp JOIN FETCH sp.projectType")
    List<SchoolProject> findAllWithProjectType();
    
    @Query(value = "SELECT COALESCE(pu.progress_percentage, 0.0) FROM project_updates pu WHERE pu.project_id = :projectId AND pu.progress_percentage IS NOT NULL ORDER BY pu.created_at DESC LIMIT 1", nativeQuery = true)
    Double getLatestCompletionRate(@Param("projectId") Integer projectId);

}