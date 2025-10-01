package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.NgoProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface NgoProjectRepository extends JpaRepository<NgoProject, Integer> {

    // Find all projects for a specific NGO
    @Query(value = "SELECT * FROM ngo_projects WHERE ngo_id = :ngoId", nativeQuery = true)
    List<NgoProject> findByNgoId(@Param("ngoId") Integer ngoId);

    // Find projects with budget greater than a certain amount
    @Query(value = "SELECT * FROM ngo_projects WHERE budget > :budget", nativeQuery = true)
    List<NgoProject> findByBudgetGreaterThan(@Param("budget") BigDecimal budget);

    // Find projects with start date after a specific date
    @Query(value = "SELECT * FROM ngo_projects WHERE start_date > :startDate", nativeQuery = true)
    List<NgoProject> findByStartDateAfter(@Param("startDate") LocalDate startDate);

    // Find projects with a specific status
    @Query(value = "SELECT * FROM ngo_projects WHERE status = :status", nativeQuery = true)
    List<NgoProject> findByStatus(@Param("status") String status);

    // Find projects containing a keyword in the name
    @Query(value = "SELECT * FROM ngo_projects WHERE project_name LIKE %:keyword%", nativeQuery = true)
    List<NgoProject> findByNameContaining(@Param("keyword") String keyword);

    // Find projects containing a keyword in the description
    @Query(value = "SELECT * FROM ngo_projects WHERE project_description LIKE %:keyword%", nativeQuery = true)
    List<NgoProject> findByDescriptionContaining(@Param("keyword") String keyword);
}
