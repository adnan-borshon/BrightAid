package com.example.Bright_Aid.Dto;

import jakarta.validation.constraints.NotNull;
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
public class NgoProjectSchoolDto {

    private Integer npsId;

    @NotNull(message = "NGO Project ID is required")
    private Integer ngoProjectId;

    private String ngoProjectName;
    private String ngoProjectDescription;
    private String ngoName;

    @NotNull(message = "School ID is required")
    private Integer schoolId;

    private String schoolName;
    private String schoolAddress;
    private String schoolEmail;

    private String projectNameForSchool;

    @NotNull(message = "Project Type ID is required")
    private Integer projectTypeId;

    private String projectTypeName;
    private String projectTypeDescription;

    private String projectDescriptionForSchool;

    @Builder.Default
    private String participationStatus = "SELECTED";

    private BigDecimal allocatedBudget;

    @Builder.Default
    private BigDecimal utilizedBudget = BigDecimal.ZERO;

    private LocalDateTime joinedAt;

    // Additional computed fields
    private BigDecimal remainingBudget;

    @Builder.Default
    private Double budgetUtilizationPercentage = 0.0;
}