package com.example.Bright_Aid.Dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NgoProjectDonationsDTO {

    private Integer projectDonationId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private BigDecimal amount;

    private LocalDateTime donatedAt;

    private String donationType;

    private String message;

    private LocalDateTime paymentCompletedAt;

    private String paymentStatus;

    private Integer projectId;

    private Integer ngoId;

    private Integer transactionId;
}
