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
}
