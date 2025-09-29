package com.example.Bright_Aid.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ngo_project_schools")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class NgoProjectSchool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nps_id")
    @EqualsAndHashCode.Include
    private Integer npsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ngo_project_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private NgoProject ngoProject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private School school;

    @Column(name = "project_name_for_school")
    private String projectNameForSchool;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_type_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private ProjectType projectType;

    @Lob
    @Column(name = "project_description_for_school")
    private String projectDescriptionForSchool;

    @Enumerated(EnumType.STRING)
    @Column(name = "participation_status", nullable = false)
    @Builder.Default
    private ParticipationStatus participationStatus = ParticipationStatus.SELECTED;

    @Column(name = "allocated_budget", precision = 15, scale = 2)
    private BigDecimal allocatedBudget;

    @Column(name = "utilized_budget", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal utilizedBudget = BigDecimal.ZERO;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;



    public enum ParticipationStatus {
        SELECTED, ACTIVE, COMPLETED, WITHDRAWN
    }
}
