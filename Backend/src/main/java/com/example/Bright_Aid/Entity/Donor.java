package com.example.Bright_Aid.Entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "donors")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Donor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "donor_id")
    @EqualsAndHashCode.Include
    private Integer donorId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @NotNull
    private User user;

    @Column(name = "organization_name")
    private String organizationName;

    @Column(name = "tax_id")
    private String taxId;

    @Column(name = "is_anonymous", nullable = false)
    @Builder.Default
    private Boolean isAnonymous = false;

    @Column(name = "total_donated", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal totalDonated = BigDecimal.ZERO;

    @Column(name = "total_schools_supported")
    @Builder.Default
    private Integer totalSchoolsSupported = 0;

    @Column(name = "total_students_sponsored")
    @Builder.Default
    private Integer totalStudentsSponsored = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "division_id")
    @ToString.Exclude
    private Division division;

    @OneToMany(mappedBy = "donor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<DonorGamification> gamifications;


}