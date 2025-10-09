package com.example.Bright_Aid.Dto;

import com.example.Bright_Aid.Entity.FundUtilization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FundUtilizationDto {
    private Integer utilizationId;
    private Integer donationId;
    private Integer projectId;
    private BigDecimal amountUsed;
    private String specificPurpose;
    private String detailedDescription;
    private String vendorName;
    private String billInvoiceNumber;
    private String receiptImageUrl;
    private LocalDate utilizationDate;
    private FundUtilization.UtilizationStatus utilizationStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Related entity data for frontend
    private DonationDto donation;
    private SchoolProjectDto project;
}