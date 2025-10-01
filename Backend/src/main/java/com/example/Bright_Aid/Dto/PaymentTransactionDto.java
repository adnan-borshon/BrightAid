package com.example.Bright_Aid.Dto;

import com.example.Bright_Aid.Entity.PaymentTransaction;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTransactionDto {

    private Integer transactionId;
    private Integer donorId;
    private Integer donationId;
    private String transactionReference;
    private BigDecimal amount;
    private String currency;
    private PaymentTransaction.TransactionType transactionType;
    private PaymentTransaction.PaymentMethod paymentMethod;
    private PaymentTransaction.TransactionStatus status;
    private String gatewayResponseCode;
    private String gatewayResponseMessage;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private LocalDateTime initiatedAt;
    private LocalDateTime completedAt;
}
