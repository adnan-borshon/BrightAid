package com.example.Bright_Aid.Dto;

import com.example.Bright_Aid.Entity.FinancialReport;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
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
public class FinancialReportDto {

    private Integer reportId;

    @NotNull(message = "Report type is required")
    private FinancialReport.ReportType reportType;

    @NotNull(message = "Report period start date is required")
    private LocalDate reportPeriodStart;

    @NotNull(message = "Report period end date is required")
    private LocalDate reportPeriodEnd;

    private Integer schoolId;

    private Integer ngoId;

    private Integer donorId;

    private String reportData;

    @DecimalMin(value = "0.0", message = "Total donations must be non-negative")
    private BigDecimal totalDonations;

    @DecimalMin(value = "0.0", message = "Total utilized must be non-negative")
    private BigDecimal totalUtilized;

    @DecimalMin(value = "0.0", message = "Pending amount must be non-negative")
    private BigDecimal pendingAmount;

    @DecimalMin(value = "0.0", message = "Transparency score must be non-negative")
    private BigDecimal transparencyScore;

    private Integer generatedBy;

    private String reportFileUrl;

    private LocalDateTime generatedAt;
}