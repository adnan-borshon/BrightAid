package com.example.Bright_Aid.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ngo_student_donations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NgoStudentDonations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer studentDonationId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    private LocalDateTime donatedAt;

    @Enumerated(EnumType.STRING)
    private DonationType donationType;

    @Column(columnDefinition = "text")
    private String donorMessage;

    private Boolean isAnonymous;

    private LocalDateTime paymentCompletedAt;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private Integer studentId;

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
