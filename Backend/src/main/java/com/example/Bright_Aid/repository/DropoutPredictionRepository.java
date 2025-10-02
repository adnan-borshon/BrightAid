package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.DropoutPrediction;
import com.example.Bright_Aid.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DropoutPredictionRepository extends JpaRepository<DropoutPrediction, Integer> {

    // Get all predictions for a specific student
    List<DropoutPrediction> findByStudent(Student student);

    // Get predictions by risk status
    List<DropoutPrediction> findByRiskStatus(DropoutPrediction.RiskStatus riskStatus);

    // Custom query: average attendance rate for a student
    @Query("SELECT AVG(dp.attendanceRate) FROM DropoutPrediction dp WHERE dp.student = :student")
    Double findAverageAttendanceRate(@Param("student") Student student);

    // Native query to calculate risk factor score and determine risk status
    @Query(value = "SELECT " +
            "CASE " +
            "WHEN (" +
            "  (CASE WHEN s.father_alive = 0 AND s.mother_alive = 0 THEN 4 " +
            "        WHEN s.father_alive = 0 AND s.mother_alive = 1 THEN 3 " +
            "        WHEN s.father_alive = 1 AND s.mother_alive = 0 THEN 2 " +
            "        ELSE 1 END) + " +
            "  (CASE WHEN s.family_income < 12000 THEN 3 " +
            "        WHEN s.family_income BETWEEN 12001 AND 40000 THEN 2 " +
            "        ELSE 1 END) + " +
            "  (CASE WHEN :attendanceRate < 30 THEN 3 " +
            "        WHEN :attendanceRate BETWEEN 30 AND 60 THEN 2 " +
            "        ELSE 1 END)" +
            ") >= 8 THEN 'HIGH' " +
            "WHEN (" +
            "  (CASE WHEN s.father_alive = 0 AND s.mother_alive = 0 THEN 4 " +
            "        WHEN s.father_alive = 0 AND s.mother_alive = 1 THEN 3 " +
            "        WHEN s.father_alive = 1 AND s.mother_alive = 0 THEN 2 " +
            "        ELSE 1 END) + " +
            "  (CASE WHEN s.family_income < 12000 THEN 3 " +
            "        WHEN s.family_income BETWEEN 12001 AND 40000 THEN 2 " +
            "        ELSE 1 END) + " +
            "  (CASE WHEN :attendanceRate < 30 THEN 3 " +
            "        WHEN :attendanceRate BETWEEN 30 AND 60 THEN 2 " +
            "        ELSE 1 END)" +
            ") >= 5 THEN 'MEDIUM' " +
            "ELSE 'LOW' END as risk_status " +
            "FROM students s WHERE s.student_id = :studentId", nativeQuery = true)
    String calculateRiskStatus(@Param("studentId") Integer studentId, @Param("attendanceRate") Double attendanceRate);

    // Native query to get the actual risk factor score
    @Query(value = "SELECT " +
            "(CASE WHEN s.father_alive = 0 AND s.mother_alive = 0 THEN 4 " +
            "      WHEN s.father_alive = 0 AND s.mother_alive = 1 THEN 3 " +
            "      WHEN s.father_alive = 1 AND s.mother_alive = 0 THEN 2 " +
            "      ELSE 1 END) + " +
            "(CASE WHEN s.family_income < 12000 THEN 3 " +
            "      WHEN s.family_income BETWEEN 12001 AND 40000 THEN 2 " +
            "      ELSE 1 END) + " +
            "(CASE WHEN :attendanceRate < 30 THEN 3 " +
            "      WHEN :attendanceRate BETWEEN 30 AND 60 THEN 2 " +
            "      ELSE 1 END) as risk_factor " +
            "FROM students s WHERE s.student_id = :studentId", nativeQuery = true)
    Integer calculateRiskFactor(@Param("studentId") Integer studentId, @Param("attendanceRate") Double attendanceRate);
}
