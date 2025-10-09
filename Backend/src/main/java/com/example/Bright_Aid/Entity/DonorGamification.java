package com.example.Bright_Aid.Entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "donor_gamification")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DonorGamification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gamification_id")
    @EqualsAndHashCode.Include
    private Integer gamificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private Donor donor;

    @Column(name = "total_points", nullable = false)
    @Builder.Default
    private Integer totalPoints = 0;

    @Column(name = "impact_score", nullable = false)
    @Builder.Default
    private Double impactScore = 0.0;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "badges_earned", columnDefinition = "JSON")
    private List<String> badgesEarned;

    // Denormalized donor name
    @Column(name = "donor_name")
    private String donorName;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    // Set donor name before saving
    @PrePersist
    @PreUpdate
    private void updateDonorName() {
        if (donor != null && donor.getDonorName() != null) {
            this.donorName = donor.getDonorName();
        } else {
            this.donorName = "Unknown Donor";
        }
    }
}