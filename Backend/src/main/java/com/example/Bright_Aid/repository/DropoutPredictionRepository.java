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

    // Get predictions by risk level
    List<DropoutPrediction> findByRiskLevel(DropoutPrediction.RiskLevel riskLevel);

    // Custom query: highest overall risk score
    @Query("SELECT dp FROM DropoutPrediction dp WHERE dp.overallRiskScore = (SELECT MAX(d.overallRiskScore) FROM DropoutPrediction d)")
    List<DropoutPrediction> findHighestRiskPredictions();

    // Custom query: average attendance rate for a student
    @Query("SELECT AVG(dp.attendanceRate) FROM DropoutPrediction dp WHERE dp.student = :student")
    Double findAverageAttendanceRate(@Param("student") Student student);

    // Custom query: find predictions containing a specific calculated risk factor
    @Query("SELECT dp FROM DropoutPrediction dp WHERE dp.calculatedRiskFactors IS NOT NULL")
    List<DropoutPrediction> findByCalculatedRiskFactor(@Param("factor") String factor);
}
