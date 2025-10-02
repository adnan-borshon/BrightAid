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
    private Double attendanceRate;
    private DropoutPrediction.RiskStatus riskStatus;
    private LocalDateTime lastCalculated;

    // Convert Entity → DTO
    public static DropoutPredictionDto fromEntity(DropoutPrediction prediction) {
        return DropoutPredictionDto.builder()
                .predictionId(prediction.getPredictionId())
                .studentId(prediction.getStudent().getStudentId())
                .attendanceRate(prediction.getAttendanceRate())
                .riskStatus(prediction.getRiskStatus())
                .lastCalculated(prediction.getLastCalculated())
                .build();
    }
}
