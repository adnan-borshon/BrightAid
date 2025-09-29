package com.example.Bright_Aid.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectUpdateDto {

    private Integer updateId;

    @NotNull(message = "Project ID is required")
    private Integer projectId;

    @NotNull(message = "Updated by user ID is required")
    private Integer updatedBy;

    private String updateTitle;

    private String updateDescription;

    private BigDecimal progressPercentage;

    private BigDecimal amountUtilized;

    private List<String> imagesUrls;
}