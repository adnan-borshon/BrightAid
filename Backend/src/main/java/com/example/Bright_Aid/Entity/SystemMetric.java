package com.example.Bright_Aid.Entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "system_metrics")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metric_id")
    private Integer metricId;

    @Column(name = "metric_date", nullable = false)
    private LocalDate metricDate;

    @Column(name = "total_active_schools", nullable = false)
    private Integer totalActiveSchools;

    @Column(name = "total_verified_schools", nullable = false)
    private Integer totalVerifiedSchools;

    @Column(name = "total_pending_verifications", nullable = false)
    private Integer totalPendingVerifications;

    @Column(name = "total_active_students", nullable = false)
    private Integer totalActiveStudents;

    @Column(name = "total_donors", nullable = false)
    private Integer totalDonors;

    @Column(name = "total_ngos", nullable = false)
    private Integer totalNgos;

    @Column(name = "total_active_projects", nullable = false)
    private Integer totalActiveProjects;

    @Column(name = "completed_projects", nullable = false)
    private Integer completedProjects;

    @Column(name = "total_funds_raised", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalFundsRaised;

    @Column(name = "total_funds_utilized", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalFundsUtilized;

    @Column(name = "utilization_rate", precision = 5, scale = 2)
    private BigDecimal utilizationRate;

    @Column(name = "high_risk_students", nullable = false)
    private Integer highRiskStudents;

    @Column(name = "average_attendance_rate", precision = 5, scale = 2)
    private BigDecimal averageAttendanceRate;

    @Column(name = "calculated_at", nullable = false)
    private LocalDateTime calculatedAt;
}