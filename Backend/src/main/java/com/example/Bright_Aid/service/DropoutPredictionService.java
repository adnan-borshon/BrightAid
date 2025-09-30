package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Entity.DropoutPrediction;
import com.example.Bright_Aid.Entity.Student;
import com.example.Bright_Aid.repository.DropoutPredictionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DropoutPredictionService {

    private final DropoutPredictionRepository repository;

    public DropoutPrediction calculateAndSavePrediction(Student student,
                                                        double attendanceRate,
                                                        double familyIncome,
                                                        boolean fatherAlive,
                                                        boolean motherAlive,
                                                        String interventionNotes) {

        DropoutPrediction prediction = new DropoutPrediction();
        prediction.setStudent(student);

        // Attendance Score
        int attendanceScore;
        if (attendanceRate >= 75) {
            attendanceScore = (int) Math.round(100 - attendanceRate);
        } else if (attendanceRate >= 50) {
            attendanceScore = (int) Math.round(26 + (74 - attendanceRate));
        } else {
            double normalized = (50 - attendanceRate) / 50.0;
            attendanceScore = (int) Math.round(51 + normalized * 49);
        }

        // Family Income Score
        int incomeScore;
        if (familyIncome >= 20000) {
            double cappedIncome = Math.min(familyIncome, 50000);
            double normalized = (cappedIncome - 20000) / 30000.0;
            incomeScore = (int) Math.round(25 * (1 - normalized));
        } else if (familyIncome >= 12000) {
            incomeScore = (int) Math.round(50 - ((familyIncome - 12000) / 8000.0 * 24));
        } else {
            double minIncome = 5000;
            double cappedIncome = Math.max(familyIncome, 0);
            double normalized = cappedIncome / minIncome;
            incomeScore = (int) Math.round(100 - (Math.min(normalized, 1) * 49));
        }

        // Parent Status Score
        int parentScore;
        if (fatherAlive && motherAlive) parentScore = 0;
        else if (!fatherAlive && !motherAlive) parentScore = 100;
        else if (!fatherAlive) parentScore = 60;
        else parentScore = 40;

        // Overall Score
        int overallScore = (int) Math.round(attendanceScore * 0.45 + incomeScore * 0.35 + parentScore * 0.20);

        DropoutPrediction.RiskLevel riskLevel;
        if (overallScore >= 70) riskLevel = DropoutPrediction.RiskLevel.CRITICAL;
        else if (overallScore >= 50) riskLevel = DropoutPrediction.RiskLevel.HIGH;
        else if (overallScore >= 30) riskLevel = DropoutPrediction.RiskLevel.MEDIUM;
        else riskLevel = DropoutPrediction.RiskLevel.LOW;

        // Risk Factors
        List<String> riskFactors = new ArrayList<>();
        if (attendanceScore >= 51) riskFactors.add("Chronic absenteeism (<50% attendance)");
        else if (attendanceScore >= 26) riskFactors.add("Low attendance (50-75%)");

        if (incomeScore >= 51) riskFactors.add("Extreme poverty (<৳12,000/month)");
        else if (incomeScore >= 26) riskFactors.add("Low family income (৳12,000-20,000/month)");

        if (parentScore == 100) riskFactors.add("Orphan - both parents deceased");
        else if (parentScore >= 40) riskFactors.add("Single parent household");

        // Save
        prediction.setAttendanceRate(attendanceScore);
        prediction.setFamilyIncomeScore(incomeScore);
        prediction.setParentStatusScore(parentScore);
        prediction.setOverallRiskScore(overallScore);
        prediction.setRiskLevel(riskLevel);
        prediction.setCalculatedRiskFactors(riskFactors);
        prediction.setInterventionNotes(interventionNotes);
        prediction.setLastCalculated(LocalDateTime.now());

        return repository.save(prediction);
    }
}
