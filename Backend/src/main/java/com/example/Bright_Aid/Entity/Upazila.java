package com.example.Bright_Aid.Entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "upazilas")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Upazila {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "upazila_id")
    @EqualsAndHashCode.Include
    private Integer upazilaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private District district;

    @NotBlank
    @Column(name = "upazila_name", nullable = false, length = 100)
    private String upazilaName;

    @NotBlank
    @Column(name = "upazila_code", unique = true, nullable = false, length = 20)
    private String upazilaCode;

    @OneToMany(mappedBy = "upazila", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<School> schools;
}