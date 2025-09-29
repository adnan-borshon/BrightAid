package com.example.Bright_Aid.Dto;

import com.example.Bright_Aid.Entity.FundUtilization;
import jakarta.validation.constraints.NotNull;
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
public class FundUtilizationDto {

    private Integer utilizationId;

    @NotNull(message = "Donation ID is required")
    private Integer donationId;

    @NotNull(message = "Project ID is required")
    private Integer projectId;

    @NotNull(message = "Amount used is required")
    @DecimalMin(value = "0.01", message = "Amount used must be greater than 0")
    private BigDecimal amountUsed;

    private String specificPurpose;

    private String detailedDescription;

    private String vendorName;

    private String billInvoiceNumber;

    private String receiptImageUrl;

    private LocalDate utilizationDate;

    @NotNull(message = "Approved by is required")
    private Integer approvedBy;

    @Builder.Default
    private FundUtilization.UtilizationStatus utilizationStatus = FundUtilization.UtilizationStatus.PENDING;

    private List<Integer> transparencyRecordIds;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
