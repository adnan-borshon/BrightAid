package com.example.Bright_Aid.Dto;

import com.example.Bright_Aid.Entity.NgoProject;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NgoProjectDto {

    private Integer ngoProjectId;
    private Integer ngoId;
    private String projectName;
    private String projectDescription;
    private Integer projectTypeId;
    private BigDecimal budget;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status; // PLANNED, ACTIVE, COMPLETED, CANCELLED
}
