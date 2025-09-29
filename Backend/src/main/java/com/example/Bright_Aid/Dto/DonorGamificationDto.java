package com.example.Bright_Aid.Dto;

import lombok.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonorGamificationDto {

    private Integer gamificationId;

    @NotNull(message = "Donor ID is required")
    private Integer donorId;

    @Min(value = 0, message = "Total points must be non-negative")
    @Builder.Default
    private Integer totalPoints = 0;

    private String currentLevel;

    private List<String> badgesEarned;

    private Integer rankingPosition;

    private LocalDateTime lastUpdated;

    // Nested DTO for donor information (optional, for response enrichment)
    private DonorBasicDto donor;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DonorBasicDto {
        private Integer donorId;
        private String organizationName;
        private Boolean isAnonymous;
        private String userName; // From User entity
        private String userEmail; // From User entity
    }
}