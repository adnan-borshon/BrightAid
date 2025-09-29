package com.example.Bright_Aid.Dto;

import com.example.Bright_Aid.Entity.DropoutPrediction;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DropoutPredictionDto {

    private Integer predictionId;

    @NotNull(message = "Student ID is required")
    private Integer studentId;

    @DecimalMin(value = "0.0000", message = "Attendance rate must be between 0 and 1")
    @DecimalMax(value = "1.0000", message = "Attendance rate must be between 0 and 1")
    private BigDecimal attendanceRate;

    @DecimalMin(value = "0.0000", message = "Family income score must be between 0 and 1")
    @DecimalMax(value = "1.0000", message = "Family income score must be between 0 and 1")
    private BigDecimal familyIncomeScore;

    @DecimalMin(value = "0.0000", message = "Parent status score must be between 0 and 1")
    @DecimalMax(value = "1.0000", message = "Parent status score must be between 0 and 1")
    private BigDecimal parentStatusScore;

    @DecimalMin(value = "0.0000", message = "Overall risk score must be between 0 and 1")
    @DecimalMax(value = "1.0000", message = "Overall risk score must be between 0 and 1")
    private BigDecimal overallRiskScore;

    @NotNull(message = "Risk level is required")
    private DropoutPrediction.RiskLevel riskLevel;

    private List<String> calculatedRiskFactors;

    @NotNull(message = "Prediction date is required")
    private LocalDate predictionDate;

    @Builder.Default
    private Boolean interventionTaken = false;

    private String interventionNotes;

    private LocalDateTime lastCalculated;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
