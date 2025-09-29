package com.example.Bright_Aid.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolDocumentDto {

    private Integer documentId;

    @NotNull(message = "School ID is required")
    private Integer schoolId;

    @NotNull(message = "Document type is required")
    private String documentType;

    @NotBlank(message = "Document title is required")
    private String documentTitle;

    private String documentDescription;

    private String fileUrl;

    private String fileHash;

    private LocalDate uploadDate;

    @NotNull(message = "Uploaded by is required")
    private Integer uploadedBy;

    @Builder.Default
    private Boolean isCurrent = true;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}