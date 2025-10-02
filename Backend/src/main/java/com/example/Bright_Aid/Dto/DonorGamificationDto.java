package com.example.Bright_Aid.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class DonorGamificationDto {

    // For response
    private Integer gamificationId;
    private Integer donorId;
    private String donorName;
    private String organizationName;
    private Integer totalPoints;
    private String currentLevel;
    private List<String> badgesEarned;
    private LocalDateTime lastUpdated;
    
    // Calculated dynamically - not stored in database
    private Integer rankingPosition;
    
    // Progress to next level fields
    private Integer pointsToNextLevel;
    private Double progressPercentage;
    private String nextLevel;

    // For request - hidden from JSON response
    @JsonIgnore
    private Integer donorIdRequest;
    @JsonIgnore
    private Integer totalPointsRequest;

    @JsonIgnore
    private List<String> badgesEarnedRequest;
}