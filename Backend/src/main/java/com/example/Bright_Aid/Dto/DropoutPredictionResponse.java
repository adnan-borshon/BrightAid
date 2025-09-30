package com.example.Bright_Aid.Dto;

import com.example.Bright_Aid.Entity.DropoutPrediction;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DropoutPredictionResponse {
    private Integer predictionId;
    private Integer studentId;
    private Double attendanceRate;
    private Double familyIncomeScore;
    private Double parentStatusScore;
    private Double overallRiskScore;
    private DropoutPrediction.RiskLevel riskLevel;
    private List<String> calculatedRiskFactors;
    private String interventionNotes;
    private LocalDateTime lastCalculated;

    public static DropoutPredictionResponse from(DropoutPrediction prediction) {
        DropoutPredictionResponse response = new DropoutPredictionResponse();
        response.setPredictionId(prediction.getPredictionId());
        response.setStudentId(prediction.getStudent().getStudentId());
        response.setAttendanceRate(prediction.getAttendanceRate() != null ? prediction.getAttendanceRate().doubleValue() : null);
        response.setFamilyIncomeScore(prediction.getFamilyIncomeScore() != null ? prediction.getFamilyIncomeScore().doubleValue() : null);
        response.setParentStatusScore(prediction.getParentStatusScore() != null ? prediction.getParentStatusScore().doubleValue() : null);
        response.setOverallRiskScore(prediction.getOverallRiskScore() != null ? prediction.getOverallRiskScore().doubleValue() : null);
        response.setRiskLevel(prediction.getRiskLevel());
        response.setCalculatedRiskFactors(prediction.getCalculatedRiskFactors());
        response.setInterventionNotes(prediction.getInterventionNotes());
        response.setLastCalculated(prediction.getLastCalculated());
        return response;
    }
}