package com.example.Bright_Aid.Entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "admin_activities")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class AdminActivity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    @EqualsAndHashCode.Include
    private Integer activityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private Admin admin;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_type", nullable = false)
    private ActivityType activityType;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_entity", nullable = false)
    private TargetEntity targetEntity;

    @Column(name = "target_id", nullable = false)
    @NotNull
    private Integer targetId;

    @Lob
    @Column(name = "activity_description")
    private String activityDescription;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "before_data", columnDefinition = "JSON")
    private String beforeData;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "after_data", columnDefinition = "JSON")
    private String afterData;

    public enum ActivityType {
        VERIFICATION, APPROVAL, REJECTION, REPORT_GENERATION, FUND_APPROVAL
    }

    public enum TargetEntity {
        SCHOOL, NGO, DONOR, PROJECT, USER, FUND_UTILIZATION
    }
}