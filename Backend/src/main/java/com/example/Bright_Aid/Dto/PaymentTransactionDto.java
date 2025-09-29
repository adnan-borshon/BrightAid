package com.example.Bright_Aid.Dto;

import com.example.Bright_Aid.Entity.PaymentTransaction;
import jakarta.validation.constraints.NotBlank;
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
public class PaymentTransactionDto {

    private Integer transactionId;

    @NotNull(message = "Donor ID is required")
    private Integer donorId;

    private String donorName;
    private String donorEmail;

    private Integer donationId;

    @NotBlank(message = "Transaction reference is required")
    private String transactionReference;

    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    @Builder.Default
    private String currency = "BDT";

    @NotNull(message = "Transaction type is required")
    private PaymentTransaction.TransactionType transactionType;

    @NotNull(message = "Payment method is required")
    private PaymentTransaction.PaymentMethod paymentMethod;

    @Builder.Default
    private PaymentTransaction.TransactionStatus status = PaymentTransaction.TransactionStatus.PENDING;

    private String gatewayResponseCode;

    private String gatewayResponseMessage;

    private String customerName;

    private String customerEmail;

    private String customerPhone;

    private LocalDateTime initiatedAt;

    private LocalDateTime completedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    private String username;
}