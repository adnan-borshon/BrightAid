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
    private DropoutPrediction.RiskStatus riskStatus;
    private LocalDateTime lastCalculated;

    public static DropoutPredictionResponse from(DropoutPrediction prediction) {
        DropoutPredictionResponse response = new DropoutPredictionResponse();
        response.setPredictionId(prediction.getPredictionId());
        response.setStudentId(prediction.getStudent().getStudentId());
        response.setAttendanceRate(prediction.getAttendanceRate());
        response.setRiskStatus(prediction.getRiskStatus());
        response.setLastCalculated(prediction.getLastCalculated());
        return response;
    }
}