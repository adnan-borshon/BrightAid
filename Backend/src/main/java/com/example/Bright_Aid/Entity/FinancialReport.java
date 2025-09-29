package com.example.Bright_Aid.Entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "financial_reports")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reportId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportType reportType;

    @Column(nullable = false)
    private LocalDate reportPeriodStart;

    @Column(nullable = false)
    private LocalDate reportPeriodEnd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ngo_id")
    private Ngo ngo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id")
    private Donor donor;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String reportData;
    // JSON with financial metrics

    private BigDecimal totalDonations;
    private BigDecimal totalUtilized;
    private BigDecimal pendingAmount;
    private BigDecimal transparencyScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generated_by")
    private Admin generatedBy;

    private String reportFileUrl;

    @CreationTimestamp
    private LocalDateTime generatedAt;

    // ✅ Enum defined inside the entity
    public enum ReportType {
        DAILY, WEEKLY, MONTHLY, QUARTERLY, YEARLY, CUSTOM
    }
}

