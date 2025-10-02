package com.example.Bright_Aid.Dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DonorStatsDto {
    private Integer donorId;
    private String donorName;
    private String taxId;
    private Boolean isAnonymous;
    
    // Calculated statistics
    private BigDecimal totalDonated;
    private Integer totalSchoolsSupported;
    private Integer totalStudentsSponsored;
    private Integer totalProjectsDonated;
}