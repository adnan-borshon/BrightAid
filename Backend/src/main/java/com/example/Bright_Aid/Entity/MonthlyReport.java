package com.example.Bright_Aid.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "monthly_reports")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class MonthlyReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    @EqualsAndHashCode.Include
    private Integer reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    @ToString.Exclude
    private School school;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ngo_id")
    @ToString.Exclude
    private Ngo ngo;

    @Column(name = "month", nullable = false)
    @Min(1)
    @Max(12)
    @NotNull
    private Integer month;

    @Column(name = "year", nullable = false)
    @Min(2000)
    @NotNull
    private Integer year;

    @Column(name = "total_students")
    @Min(0)
    private Integer totalStudents;

    @Column(name = "present_students")
    @Min(0)
    private Integer presentStudents;

    @Column(name = "average_attendance", precision = 5, scale = 2)
    @DecimalMin("0.0")
    private BigDecimal averageAttendance;

    @Column(name = "new_enrollments")
    @Min(0)
    private Integer newEnrollments;

    @Column(name = "dropouts")
    @Min(0)
    private Integer dropouts;

    @Column(name = "high_risk_students")
    @Min(0)
    private Integer highRiskStudents;

    @Column(name = "funds_received", precision = 15, scale = 2)
    @DecimalMin("0.0")
    private BigDecimal fundsReceived;

    @Column(name = "funds_utilized", precision = 15, scale = 2)
    @DecimalMin("0.0")
    private BigDecimal fundsUtilized;

    @Column(name = "funds_pending", precision = 15, scale = 2)
    @DecimalMin("0.0")
    private BigDecimal fundsPending;

    @Column(name = "active_projects")
    @Min(0)
    private Integer activeProjects;

    @Column(name = "completed_projects")
    @Min(0)
    private Integer completedProjects;

    @Column(name = "generated_at")
    private LocalDateTime generatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generated_by")
    @ToString.Exclude
    private Admin generatedBy;
}