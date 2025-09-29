package com.example.Bright_Aid.Entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "fund_utilization")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class FundUtilization extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "utilization_id")
    @EqualsAndHashCode.Include
    private Integer utilizationId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donation_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private Donation donation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private SchoolProject project;

    @Column(name = "amount_used", precision = 15, scale = 2, nullable = false)
    @NotNull
    private BigDecimal amountUsed;

    @Column(name = "specific_purpose")
    private String specificPurpose;

    @Lob
    @Column(name = "detailed_description")
    private String detailedDescription;

    @Column(name = "vendor_name")
    private String vendorName;

    @Column(name = "bill_invoice_number")
    private String billInvoiceNumber;

    @Column(name = "receipt_image_url")
    private String receiptImageUrl;

    @Column(name = "utilization_date")
    private LocalDate utilizationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by", nullable = false)
    @NotNull
    @ToString.Exclude
    private Admin approvedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "utilization_status", nullable = false)
    @Builder.Default
    private UtilizationStatus utilizationStatus = UtilizationStatus.PENDING;

    @OneToMany(mappedBy = "utilization", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<FundTransparency> transparencyRecords;

    public enum UtilizationStatus {
        PENDING, APPROVED, COMPLETED
    }
}
