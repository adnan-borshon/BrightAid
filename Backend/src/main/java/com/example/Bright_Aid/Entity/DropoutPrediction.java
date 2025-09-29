package com.example.Bright_Aid.Entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "dropout_predictions")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class DropoutPrediction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prediction_id")
    @EqualsAndHashCode.Include
    private Integer predictionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private Student student;

    @Column(name = "attendance_rate", precision = 5, scale = 4)
    private BigDecimal attendanceRate;

    @Column(name = "family_income_score", precision = 5, scale = 4)
    private BigDecimal familyIncomeScore;

    @Column(name = "parent_status_score", precision = 5, scale = 4)
    private BigDecimal parentStatusScore;

    @Column(name = "overall_risk_score", precision = 5, scale = 4)
    private BigDecimal overallRiskScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false)
    private RiskLevel riskLevel;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "calculated_risk_factors", columnDefinition = "JSON")
    private List<String> calculatedRiskFactors;

    @Column(name = "prediction_date", nullable = false)
    private LocalDate predictionDate;

    @Column(name = "intervention_taken", nullable = false)
    @Builder.Default
    private Boolean interventionTaken = false;

    @Lob
    @Column(name = "intervention_notes")
    private String interventionNotes;

    @Column(name = "last_calculated", nullable = false)
    private LocalDateTime lastCalculated;

    public enum RiskLevel {
        LOW, MEDIUM, HIGH, CRITICAL
    }
}
