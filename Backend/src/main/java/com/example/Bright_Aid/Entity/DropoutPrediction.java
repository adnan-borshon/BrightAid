package com.example.Bright_Aid.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Student student;

    @Column(name = "attendance_rate")
    private Integer attendanceRate;

    @Column(name = "family_income_score")
    private Integer familyIncomeScore;

    @Column(name = "parent_status_score")
    @JsonProperty("parentStatusScore")
    @EqualsAndHashCode.Include
    private Integer parentStatusScore;

    @Column(name = "overall_risk_score")
    private Integer overallRiskScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false)
    private RiskLevel riskLevel;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "calculated_risk_factors", columnDefinition = "JSON")
    private List<String> calculatedRiskFactors;

    @Lob
    @Column(name = "intervention_notes")
    private String interventionNotes;

    @Column(name = "last_calculated", nullable = false)
    private LocalDateTime lastCalculated;

    public enum RiskLevel {
        LOW, MEDIUM, HIGH, CRITICAL
    }
}
