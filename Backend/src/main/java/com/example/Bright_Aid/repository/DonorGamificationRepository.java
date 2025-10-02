package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.DonorGamification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonorGamificationRepository extends JpaRepository<DonorGamification, Integer> {

    // Find all gamifications for a specific donor
    @Query(value = "SELECT * FROM donor_gamification WHERE donor_id = :donorId", nativeQuery = true)
    List<DonorGamification> findByDonorId(@Param("donorId") Integer donorId);

    // Find gamifications with points greater than a certain value
    @Query(value = "SELECT * FROM donor_gamification WHERE total_points > :points", nativeQuery = true)
    List<DonorGamification> findByPointsGreaterThan(@Param("points") Integer points);

    // Get top N donors by total_points
    @Query(value = "SELECT * FROM donor_gamification ORDER BY total_points DESC LIMIT :limit", nativeQuery = true)
    List<DonorGamification> findTopGamifications(@Param("limit") int limit);

    // Find gamifications by current_level
    @Query(value = "SELECT * FROM donor_gamification WHERE current_level = :level", nativeQuery = true)
    List<DonorGamification> findByCurrentLevel(@Param("level") String level);

    // Find gamifications updated after a certain date
    @Query(value = "SELECT * FROM donor_gamification WHERE last_updated > :date", nativeQuery = true)
    List<DonorGamification> findUpdatedAfter(@Param("date") String date);
    
    // Find gamification by donor entity
    @Query(value = "SELECT * FROM donor_gamification WHERE donor_id = :#{#donor.donorId}", nativeQuery = true)
    java.util.Optional<DonorGamification> findByDonor(@Param("donor") com.example.Bright_Aid.Entity.Donor donor);
    
    // Find single gamification by donor ID - returns Optional for single result
    @Query(value = "SELECT * FROM donor_gamification WHERE donor_id = :donorId LIMIT 1", nativeQuery = true)
    java.util.Optional<DonorGamification> findByDonor_DonorId(@Param("donorId") Integer donorId);
    
    // Count unique schools from donor's sponsored students and projects
    @Query(value = """
        SELECT COUNT(DISTINCT school_id) FROM (
            SELECT st.school_id 
            FROM donations d 
            JOIN students st ON d.student_id = st.student_id 
            WHERE d.donor_id = :donorId AND d.student_id IS NOT NULL
            UNION
            SELECT sp.school_id 
            FROM donations d 
            JOIN school_projects sp ON d.project_id = sp.project_id 
            WHERE d.donor_id = :donorId AND d.project_id IS NOT NULL
        ) AS unique_schools
        """, nativeQuery = true)
    Integer getUniqueSchoolsCountByDonor(@Param("donorId") Integer donorId);
    
    // Calculate donor ranking based on total points using native query
    @Query(value = """
        SELECT ranking FROM (
            SELECT donor_id, 
                   total_points,
                   RANK() OVER (ORDER BY total_points DESC) as ranking
            FROM donor_gamification
        ) ranked_donors 
        WHERE donor_id = :donorId
        """, nativeQuery = true)
    Integer calculateDonorRanking(@Param("donorId") Integer donorId);
}