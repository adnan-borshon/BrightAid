package com.example.Bright_Aid.Entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "fund_transparency")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class FundTransparency extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transparency_id")
    @EqualsAndHashCode.Include
    private Integer transparencyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilization_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private FundUtilization utilization;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "before_photos", columnDefinition = "JSON")
    private List<String> beforePhotos;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "after_photos", columnDefinition = "JSON")
    private List<String> afterPhotos;

    @Lob
    @Column(name = "beneficiary_feedback")
    private String beneficiaryFeedback;

    @Column(name = "quantity_purchased", precision = 10, scale = 2)
    private BigDecimal quantityPurchased;

    @Column(name = "unit_measurement")
    private String unitMeasurement;

    @Column(name = "unit_cost", precision = 10, scale = 2)
    private BigDecimal unitCost;

    @Lob
    @Column(name = "additional_notes")
    private String additionalNotes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verified_by", nullable = false)
    @NotNull
    @ToString.Exclude
    private Admin verifiedBy;

    @Column(name = "is_public", nullable = false)
    @Builder.Default
    private Boolean isPublic = true;
}
