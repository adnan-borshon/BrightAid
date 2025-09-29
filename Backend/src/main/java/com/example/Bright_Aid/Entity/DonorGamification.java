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

    @Column(name = "current_level")
    private String currentLevel;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "badges_earned", columnDefinition = "JSON")
    private List<String> badgesEarned;

    @Column(name = "ranking_position")
    private Integer rankingPosition;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;
}
