package com.example.Bright_Aid.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NgoGamificationDTO {

    private Integer gamificationId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String badgesEarned;

    private String ngoName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime lastUpdated;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer totalPoints;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal impactScore;

    private Integer ngoId;
}
