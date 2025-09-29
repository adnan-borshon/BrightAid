package com.example.Bright_Aid.Dto;

import com.example.Bright_Aid.Entity.Donation;
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
public class DonationDto {

    private Integer donationId;

    @NotNull(message = "Donor ID is required")
    private Integer donorId;

    private Integer projectId;

    private Integer studentId;

    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    @NotNull(message = "Donation type is required")
    private Donation.DonationType donationType;

    private Integer transactionId;

    @Builder.Default
    private Donation.PaymentStatus paymentStatus = Donation.PaymentStatus.PENDING;

    @NotNull(message = "Purpose is required")
    private Donation.DonationPurpose purpose;

    private String donorMessage;

    @Builder.Default
    private Boolean isAnonymous = false;

    private LocalDateTime donatedAt;

    private LocalDateTime paymentCompletedAt;

    private Integer fundUtilizationId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}