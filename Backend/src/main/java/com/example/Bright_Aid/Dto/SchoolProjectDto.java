package com.example.Bright_Aid.Dto;

import jakarta.validation.constraints.NotBlank;
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
public class SchoolProjectDto {

    private Integer projectId;

    @NotNull(message = "School ID is required")
    private Integer schoolId;



    @NotBlank(message = "Project title is required")
    private String projectTitle;

    private String projectDescription;

    @NotNull(message = "Project type ID is required")
    private Integer projectTypeId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}