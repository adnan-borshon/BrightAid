package com.example.Bright_Aid.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ngo_project_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NgoProjectRequests {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer requestId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Integer ngoProjectId;

    @Column(nullable = false)
    private Integer schoolId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestType requestType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status = RequestStatus.PENDING;

    // Request details
    @Column(columnDefinition = "text")
    private String requestMessage;

    @Column(precision = 15, scale = 2)
    private BigDecimal requestedBudget;

    @Column(nullable = false)
    private LocalDateTime requestedAt;

    @Column(nullable = false)
    private Integer requestedByUserId;

    // Response details
    @Column(columnDefinition = "text")
    private String responseMessage;

    private LocalDateTime respondedAt;

    private Integer respondedByUserId;

    // Link to participation record (after approval)
    private Integer npsId;

    // ===================== ENUMS =====================
    public enum RequestType {
        JOIN_REQUEST,
        INVITE
        // Add other types if necessary
    }

    public enum RequestStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
}
