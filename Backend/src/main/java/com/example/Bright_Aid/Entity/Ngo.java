package com.example.Bright_Aid.Entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ngos")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Ngo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ngo_id")
    @EqualsAndHashCode.Include
    private Integer ngoId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @NotNull
    private User user;

    @NotBlank
    @Column(name = "ngo_name", nullable = false)
    private String ngoName;

    @NotBlank
    @Column(name = "registration_number", unique = true, nullable = false)
    private String registrationNumber;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "contact_person")
    private String contactPerson;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false)
    @Builder.Default
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;



    @OneToMany(mappedBy = "ngo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<NgoProject> projects;

    public enum VerificationStatus {
        PENDING, VERIFIED, REJECTED
    }
}