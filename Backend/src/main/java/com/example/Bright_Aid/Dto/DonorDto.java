package com.example.Bright_Aid.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonorDto {

    private Integer donorId;

    @NotNull(message = "User ID is required")
    private Integer userId;

    private String username;
    private String userEmail;
    private String userFullName;

    private String organizationName;

    private String taxId;

    @Builder.Default
    private Boolean isAnonymous = false;

    @Builder.Default
    private BigDecimal totalDonated = BigDecimal.ZERO;

    @Builder.Default
    private Integer totalSchoolsSupported = 0;

    @Builder.Default
    private Integer totalStudentsSponsored = 0;

    private Integer divisionId;

    private String divisionName;

    private List<Integer> gamificationIds;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}