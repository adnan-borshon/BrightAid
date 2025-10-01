package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.ProjectUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProjectUpdateRepository extends JpaRepository<ProjectUpdate, Integer> {

    // Find all updates for a specific project
    @Query(value = "SELECT * FROM project_updates WHERE project_id = :projectId", nativeQuery = true)
    List<ProjectUpdate> findByProjectId(@Param("projectId") Integer projectId);

    // Find updates with progress greater than a certain value
    @Query(value = "SELECT * FROM project_updates WHERE progress_percentage > :progress", nativeQuery = true)
    List<ProjectUpdate> findByProgressGreaterThan(@Param("progress") BigDecimal progress);

    // Find updates with amount utilized greater than a certain value
    @Query(value = "SELECT * FROM project_updates WHERE amount_utilized > :amount", nativeQuery = true)
    List<ProjectUpdate> findByAmountUtilizedGreaterThan(@Param("amount") BigDecimal amount);

    // Find updates containing a specific keyword in the title
    @Query(value = "SELECT * FROM project_updates WHERE update_title LIKE CONCAT('%', :keyword, '%')", nativeQuery = true)
    List<ProjectUpdate> findByTitleContaining(@Param("keyword") String keyword);

    // Find updates containing a specific keyword in the description
    @Query(value = "SELECT * FROM project_updates WHERE update_description LIKE CONCAT('%', :keyword, '%')", nativeQuery = true)
    List<ProjectUpdate> findByDescriptionContaining(@Param("keyword") String keyword);
}
