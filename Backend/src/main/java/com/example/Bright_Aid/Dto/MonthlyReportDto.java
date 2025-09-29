package com.example.Bright_Aid.Dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyReportDto {

    private Integer reportId;

    private Integer schoolId;

    private Integer ngoId;

    @NotNull(message = "Month is required")
    @Min(value = 1, message = "Month must be between 1 and 12")
    @Max(value = 12, message = "Month must be between 1 and 12")
    private Integer month;

    @NotNull(message = "Year is required")
    @Min(value = 2000, message = "Year must be 2000 or later")
    private Integer year;

    @Min(value = 0, message = "Total students must be non-negative")
    private Integer totalStudents;

    @Min(value = 0, message = "Present students must be non-negative")
    private Integer presentStudents;

    @DecimalMin(value = "0.0", message = "Average attendance must be non-negative")
    @DecimalMax(value = "100.0", message = "Average attendance cannot exceed 100%")
    private BigDecimal averageAttendance;

    @Min(value = 0, message = "New enrollments must be non-negative")
    private Integer newEnrollments;

    @Min(value = 0, message = "Dropouts must be non-negative")
    private Integer dropouts;

    @Min(value = 0, message = "High risk students must be non-negative")
    private Integer highRiskStudents;

    @DecimalMin(value = "0.0", message = "Funds received must be non-negative")
    private BigDecimal fundsReceived;

    @DecimalMin(value = "0.0", message = "Funds utilized must be non-negative")
    private BigDecimal fundsUtilized;

    @DecimalMin(value = "0.0", message = "Funds pending must be non-negative")
    private BigDecimal fundsPending;

    @Min(value = 0, message = "Active projects must be non-negative")
    private Integer activeProjects;

    @Min(value = 0, message = "Completed projects must be non-negative")
    private Integer completedProjects;

    private LocalDateTime generatedAt;

    private Integer generatedBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
