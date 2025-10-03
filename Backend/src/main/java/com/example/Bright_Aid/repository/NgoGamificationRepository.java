package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.NgoGamification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NgoGamificationRepository extends JpaRepository<NgoGamification, Integer> {

    // Example: Find by ngoId using native query
    @Query(value = "SELECT * FROM ngo_gamification WHERE ngo_id = :ngoId", nativeQuery = true)
    NgoGamification findByNgoId(@Param("ngoId") Integer ngoId);

    // Example: Find all with totalPoints greater than a value
    @Query(value = "SELECT * FROM ngo_gamification WHERE total_points > :points", nativeQuery = true)
    List<NgoGamification> findByTotalPointsGreaterThan(@Param("points") Integer points);
    
    // Check if NGO gamification exists for given ngo_id
    boolean existsByNgoId(Integer ngoId);
}
 