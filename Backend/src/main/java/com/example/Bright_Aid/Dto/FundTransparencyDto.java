package com.example.Bright_Aid.Dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FundTransparencyDto {

    private Integer transparencyId;

    @NotNull(message = "Utilization ID is required")
    private Integer utilizationId;

    private List<String> beforePhotos;

    private List<String> afterPhotos;

    private String beneficiaryFeedback;

    @DecimalMin(value = "0.0", message = "Quantity purchased must be non-negative")
    private BigDecimal quantityPurchased;

    private String unitMeasurement;

    @DecimalMin(value = "0.0", message = "Unit cost must be non-negative")
    private BigDecimal unitCost;

    private String additionalNotes;

    @NotNull(message = "Verified by is required")
    private Integer verifiedBy;

    @Builder.Default
    private Boolean isPublic = true;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
