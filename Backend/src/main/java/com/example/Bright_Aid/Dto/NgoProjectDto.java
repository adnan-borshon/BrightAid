package com.example.Bright_Aid.Dto;

import com.example.Bright_Aid.Entity.NgoProject;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
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
public class NgoProjectDto {

    private Integer ngoProjectId;

    private Integer npsId;

    private Integer ngoId;

    @NotNull(message = "Project type ID is required")
    private Integer projectTypeId;

    @DecimalMin(value = "0.0", message = "Budget must be non-negative")
    private BigDecimal budget;

    private LocalDate startDate;

    @Future(message = "End date must be in the future")
    private LocalDate endDate;

    @Builder.Default
    private NgoProject.ProjectStatus status = NgoProject.ProjectStatus.PLANNED;

    private List<Integer> schoolParticipationIds;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}