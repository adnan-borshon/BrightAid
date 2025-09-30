package com.example.Bright_Aid.Dto;

import com.example.Bright_Aid.Entity.DropoutPrediction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DropoutPredictionDto {

    private Integer predictionId;
    private Integer studentId;
    private Integer attendanceRate;
    private Integer familyIncomeScore;
    private Integer parentStatusScore;
    private Integer overallRiskScore;
    private DropoutPrediction.RiskLevel riskLevel;
    private List<String> calculatedRiskFactors;
    private String interventionNotes;
    private LocalDateTime lastCalculated;

    // Convert Entity → DTO
    public static DropoutPredictionDto fromEntity(DropoutPrediction prediction) {
        return DropoutPredictionDto.builder()
                .predictionId(prediction.getPredictionId())
                .studentId(prediction.getStudent().getStudentId())
                .attendanceRate(prediction.getAttendanceRate())
                .familyIncomeScore(prediction.getFamilyIncomeScore())
                .parentStatusScore(prediction.getParentStatusScore())
                .overallRiskScore(prediction.getOverallRiskScore())
                .riskLevel(prediction.getRiskLevel())
                .calculatedRiskFactors(prediction.getCalculatedRiskFactors())
                .interventionNotes(prediction.getInterventionNotes())
                .lastCalculated(prediction.getLastCalculated())
                .build();
    }
}
