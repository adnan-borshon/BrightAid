package com.example.Bright_Aid.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DivisionDto {

    private Integer divisionId;

    @NotBlank(message = "Division name is required")
    private String divisionName;

    @NotBlank(message = "Division code is required")
    private String divisionCode;

    private List<Integer> districtIds;

    private List<Integer> schoolIds;

    private List<Integer> donorIds;
}