package com.example.Bright_Aid.Entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "project_updates")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class ProjectUpdate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "update_id")
    @EqualsAndHashCode.Include
    private Integer updateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private SchoolProject project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", nullable = false)
    @NotNull
    @ToString.Exclude
    private User updatedBy;

    @Column(name = "update_title")
    private String updateTitle;

    @Lob
    @Column(name = "update_description")
    private String updateDescription;

    @Column(name = "progress_percentage", precision = 5, scale = 2)
    private BigDecimal progressPercentage;

    @Column(name = "amount_utilized", precision = 15, scale = 2)
    private BigDecimal amountUtilized;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "images_urls", columnDefinition = "JSON")
    private List<String> imagesUrls;


}
