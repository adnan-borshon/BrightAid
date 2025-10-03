package com.example.Bright_Aid.Dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NgoProjectRequestsDTO {

    private Integer requestId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Integer ngoProjectId;

    private Integer schoolId;

    private String requestType;

    private String status;

    private String requestMessage;

    private BigDecimal requestedBudget;

    private LocalDateTime requestedAt;

    private Integer requestedByUserId;

    private String responseMessage;

    private LocalDateTime respondedAt;

    private Integer respondedByUserId;

    private Integer npsId;
}
