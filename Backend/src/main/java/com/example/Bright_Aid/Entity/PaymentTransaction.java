package com.example.Bright_Aid.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_transactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTransaction extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transactionId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id", nullable = false)
    private Donor donor;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donation_id")
    private Donation donation;
    
    @Column(unique = true)
    private String transactionReference;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    
    @Column(nullable = false, length = 3)
    private String currency = "BDT";
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status = TransactionStatus.PENDING;
    
    private String gatewayResponseCode;
    private String gatewayResponseMessage;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    
    // Product details
    private String productName;
    private String productCategory;
    
    // SSLCommerz specific fields
    private String sessionKey;
    private String bankTransactionId;
    private String cardType;
    private String cardNo;
    
    private LocalDateTime initiatedAt = LocalDateTime.now();
    private LocalDateTime completedAt;
    
    public enum TransactionType {
        DONATION, SPONSORSHIP, SUBSCRIPTION
    }
    
    public enum PaymentMethod {
        CARD, MOBILE_BANKING, INTERNET_BANKING, CASH
    }
    
    public enum TransactionStatus {
        PENDING, SUCCESS, FAILED, CANCELLED, REFUNDED
    }
}