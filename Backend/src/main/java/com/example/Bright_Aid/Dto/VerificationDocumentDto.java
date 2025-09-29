package com.example.Bright_Aid.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationDocumentDto {

    private Integer documentId;

    @NotNull(message = "Request ID is required")
    private Integer requestId;

    private String documentType;

    private String documentName;

    private String fileUrl;

    private String fileHash;

    @Builder.Default
    private Boolean isVerified = false;

    @NotNull(message = "Admin ID is required")
    private Integer adminId;

    private LocalDateTime uploadedAt;

    private LocalDateTime verifiedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}