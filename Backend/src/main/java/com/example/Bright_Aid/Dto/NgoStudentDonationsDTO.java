package com.example.Bright_Aid.Dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NgoStudentDonationsDTO {

    private Integer studentDonationId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private BigDecimal amount;

    private LocalDateTime donatedAt;

    private String donationType;

    private String donorMessage;

    private Boolean isAnonymous;

    private LocalDateTime paymentCompletedAt;

    private String paymentStatus;

    private Integer studentId;

    private Integer ngoId;

    private Integer transactionId;
}
