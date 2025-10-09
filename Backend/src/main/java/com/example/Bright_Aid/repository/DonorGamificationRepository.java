package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.DonorGamification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonorGamificationRepository extends JpaRepository<DonorGamification, Integer> {

    Optional<DonorGamification> findByDonorDonorId(Integer donorId);

    @Query("SELECT dg FROM DonorGamification dg ORDER BY dg.totalPoints DESC")
    List<DonorGamification> findAllOrderByTotalPointsDesc();

    @Query("SELECT dg FROM DonorGamification dg WHERE dg.totalPoints >= :minPoints ORDER BY dg.totalPoints DESC")
    List<DonorGamification> findByTotalPointsGreaterThanEqualOrderByTotalPointsDesc(@Param("minPoints") Integer minPoints);

    @Query("SELECT COUNT(dg) FROM DonorGamification dg WHERE dg.totalPoints > (SELECT dg2.totalPoints FROM DonorGamification dg2 WHERE dg2.donor.donorId = :donorId)")
    Long getRankByDonorId(@Param("donorId") Integer donorId);

    @Query(value = """
        SELECT COUNT(DISTINCT COALESCE(s.school_id, sp.school_id)) 
        FROM donations d 
        LEFT JOIN students s ON d.student_id = s.student_id 
        LEFT JOIN school_projects sp ON d.project_id = sp.project_id 
        WHERE d.donor_id = :donorId AND d.payment_status = 'COMPLETED'
        """, nativeQuery = true)
    Integer getUniqueSchoolsCountByDonor(@Param("donorId") Integer donorId);
}