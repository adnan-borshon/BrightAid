package com.example.Bright_Aid.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTypeDto {

    private Integer projectTypeId;

    @NotBlank(message = "Type name is required")
    private String typeName;

    @NotBlank(message = "Type code is required")
    private String typeCode;

    private String typeDescription;

    private Boolean isActive;

    private List<Integer> projectIds;
}