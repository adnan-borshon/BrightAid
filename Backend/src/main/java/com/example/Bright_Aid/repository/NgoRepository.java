package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.Ngo;
import com.example.Bright_Aid.Entity.Ngo.VerificationStatus;
import com.example.Bright_Aid.Entity.User;
// import com.example.Bright_Aid.Entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NgoRepository extends JpaRepository<Ngo, Integer> {

    // Native query to get total amount donated by NGO
    @Query(value = "SELECT COALESCE(SUM(d.amount), 0) " +
           "FROM donations d " +
           "WHERE d.donation_id IN (" +
           "  SELECT COALESCE(donation_id, 0) FROM ngo_student_donations WHERE ngo_id = :ngoId " +
           "  UNION " +
           "  SELECT COALESCE(donation_id, 0) FROM ngo_project_donations WHERE ngo_id = :ngoId" +
           ")", 
           nativeQuery = true)
    Long getTotalDonatedByNgo(@Param("ngoId") Integer ngoId);
    
    // Native query to count unique students helped by NGO
    @Query(value = "SELECT COALESCE(COUNT(DISTINCT nsd.student_id), 0) " +
           "FROM ngo_student_donations nsd " +
           "LEFT JOIN donations d ON nsd.donation_id = d.donation_id " +
           "WHERE nsd.ngo_id = :ngoId AND (d.payment_status = 'COMPLETED' OR d.payment_status IS NULL)", 
           nativeQuery = true)
    Long getStudentsHelpedByNgo(@Param("ngoId") Integer ngoId);
    
    // Native query to count total school projects available
    @Query(value = "SELECT COUNT(*) FROM school_projects", nativeQuery = true)
    Long getSchoolProjectsCount();
    
    // Native query to count schools reached through NGO donations
    @Query(value = "SELECT COALESCE(COUNT(DISTINCT school_id), 0) FROM (" +
           "  SELECT DISTINCT COALESCE(s.school_id, 0) as school_id " +
           "  FROM schools s " +
           "  LEFT JOIN students st ON s.school_id = st.school_id " +
           "  LEFT JOIN ngo_student_donations nsd ON st.student_id = nsd.student_id " +
           "  LEFT JOIN donations d1 ON nsd.donation_id = d1.donation_id " +
           "  WHERE nsd.ngo_id = :ngoId AND (d1.payment_status = 'COMPLETED' OR d1.payment_status IS NULL) " +
           "  UNION " +
           "  SELECT DISTINCT COALESCE(sp.school_id, 0) as school_id " +
           "  FROM school_projects sp " +
           "  LEFT JOIN ngo_project_donations npd ON sp.project_id = npd.project_id " +
           "  LEFT JOIN donations d2 ON npd.donation_id = d2.donation_id " +
           "  WHERE npd.ngo_id = :ngoId AND (d2.payment_status = 'COMPLETED' OR d2.payment_status IS NULL)" +
           ") AS combined_schools WHERE school_id > 0", 
           nativeQuery = true)
    Long getSchoolsReachedByNgo(@Param("ngoId") Integer ngoId);

}