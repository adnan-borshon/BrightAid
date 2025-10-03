package com.example.Bright_Aid.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ngo_gamification")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NgoGamification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer gamificationId;

    @Column(columnDefinition = "json")
    private String badgesEarned;

    @Column(length = 255)
    private String ngoName;

    private LocalDateTime lastUpdated;

    private Integer totalPoints;

    @Column(precision = 10, scale = 2)
    private BigDecimal impactScore;

    @Column(unique = true)
    private Integer ngoId;
}
