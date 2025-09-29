package com.example.Bright_Aid.Entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "project_types")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProjectType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_type_id")
    @EqualsAndHashCode.Include
    private Integer projectTypeId;

    @NotBlank
    @Column(name = "type_name", unique = true, nullable = false)
    private String typeName;

    @NotBlank
    @Column(name = "type_code", unique = true, nullable = false)
    private String typeCode;

    @Lob
    @Column(name = "type_description")
    private String typeDescription;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @OneToMany(mappedBy = "projectType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<NgoProject> projects;


}
