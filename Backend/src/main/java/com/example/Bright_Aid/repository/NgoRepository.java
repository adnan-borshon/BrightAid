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
    @Query(value = "SELECT COALESCE(SUM(nsd.amount), 0) + COALESCE(SUM(npd.amount), 0) " +
           "FROM (SELECT amount FROM ngo_student_donations WHERE ngo_id = :ngoId) nsd " +
           "FULL OUTER JOIN (SELECT amount FROM ngo_project_donations WHERE ngo_id = :ngoId) npd ON 1=1", 
           nativeQuery = true)
    Long getTotalDonatedByNgo(@Param("ngoId") Integer ngoId);
    
    // Native query to count unique students helped by NGO
    @Query(value = "SELECT COUNT(DISTINCT student_id) FROM ngo_student_donations WHERE ngo_id = :ngoId", 
           nativeQuery = true)
    Long getStudentsHelpedByNgo(@Param("ngoId") Integer ngoId);
    
    // Native query to count total school projects available
    @Query(value = "SELECT COUNT(*) FROM school_projects", nativeQuery = true)
    Long getSchoolProjectsCount();
    
    // Native query to count schools reached through NGO donations
    @Query(value = "SELECT COUNT(DISTINCT s.school_id) " +
           "FROM schools s " +
           "JOIN students st ON s.school_id = st.school_id " +
           "JOIN ngo_student_donations nsd ON st.student_id = nsd.student_id " +
           "WHERE nsd.ngo_id = :ngoId " +
           "UNION " +
           "SELECT COUNT(DISTINCT sp.school_id) " +
           "FROM school_projects sp " +
           "JOIN ngo_project_donations npd ON sp.project_id = npd.project_id " +
           "WHERE npd.ngo_id = :ngoId", 
           nativeQuery = true)
    Long getSchoolsReachedByNgo(@Param("ngoId") Integer ngoId);

}