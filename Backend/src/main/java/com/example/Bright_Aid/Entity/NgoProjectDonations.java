package com.example.Bright_Aid.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ngo_project_donations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NgoProjectDonations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer projectDonationId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    private LocalDateTime donatedAt;

    @Enumerated(EnumType.STRING)
    private DonationType donationType;

    @Column(columnDefinition = "text")
    private String message;

    private LocalDateTime paymentCompletedAt;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(nullable = false)
    private Integer projectId;

    private Integer ngoId;

    private Integer transactionId;

    // ===================== ENUMS =====================
    public enum DonationType {
        MONTHLY,
        ONE_TIME,
        YEARLY
    }

    public enum PaymentStatus {
        COMPLETED,
        FAILED,
        PENDING
    }
}
