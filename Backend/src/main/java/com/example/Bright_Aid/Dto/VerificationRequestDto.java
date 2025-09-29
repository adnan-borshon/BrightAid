package com.example.Bright_Aid.Dto;

import com.example.Bright_Aid.Entity.VerificationRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationRequestDto {

    private Integer requestId;

    @NotNull(message = "User ID is required")
    private Integer userId;

    @NotNull(message = "Entity type is required")
    private VerificationRequest.EntityType entityType;

    @NotNull(message = "Entity ID is required")
    private Integer entityId;

    private List<String> documentsUrls;

    private String verificationNotes;

    private String adminComments;

    @Builder.Default
    private VerificationRequest.VerificationStatus status = VerificationRequest.VerificationStatus.PENDING;

    @Builder.Default
    private VerificationRequest.Priority priority = VerificationRequest.Priority.MEDIUM;

    private Integer reviewedBy;

    private LocalDateTime submittedAt;

    private LocalDateTime reviewedAt;

    private LocalDateTime deadline;

    private List<Integer> documentIds;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}