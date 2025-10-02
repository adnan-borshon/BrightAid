package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface DonorRepository extends JpaRepository<Donor, Integer> {

    // 1. Find donor by userId
    @Query("SELECT d FROM Donor d WHERE d.user.userId = :userId")
    Optional<Donor> findByUserId(@Param("userId") Integer userId);

    // 2. Get all anonymous donors
    @Query("SELECT d FROM Donor d WHERE d.isAnonymous = true")
    List<Donor> findAllAnonymousDonors();


    
    // Calculate total donated amount for a donor
    @Query(value = "SELECT COALESCE(SUM(amount), 0) FROM donation WHERE donor_id = :donorId", nativeQuery = true)
    BigDecimal calculateTotalDonated(@Param("donorId") Integer donorId);
    
    // Calculate total schools supported for a donor
    @Query(value = """
        SELECT COUNT(DISTINCT school_id) FROM (
            SELECT st.school_id 
            FROM donation d 
            JOIN student st ON d.student_id = st.student_id 
            WHERE d.donor_id = :donorId AND d.student_id IS NOT NULL
            UNION
            SELECT sp.school_id 
            FROM donation d 
            JOIN school_project sp ON d.project_id = sp.project_id 
            WHERE d.donor_id = :donorId AND d.project_id IS NOT NULL
        ) AS unique_schools
        """, nativeQuery = true)
    Integer calculateTotalSchoolsSupported(@Param("donorId") Integer donorId);
    
    // Calculate total students sponsored for a donor
    @Query(value = "SELECT COUNT(DISTINCT student_id) FROM donation WHERE donor_id = :donorId AND student_id IS NOT NULL", nativeQuery = true)
    Integer calculateTotalStudentsSponsored(@Param("donorId") Integer donorId);
    
    // Calculate total projects donated for a donor
    @Query(value = "SELECT COUNT(DISTINCT project_id) FROM donation WHERE donor_id = :donorId AND project_id IS NOT NULL", nativeQuery = true)
    Integer calculateTotalProjectsDonated(@Param("donorId") Integer donorId);

}
