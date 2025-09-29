package com.example.Bright_Aid.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistrictDto {

    private Integer districtId;

    @NotBlank(message = "District name is required")
    private String districtName;

    @NotBlank(message = "District code is required")
    private String districtCode;

    @NotNull(message = "Division ID is required")
    private Integer divisionId;

    private String divisionName;

    private List<Integer> upazilaIds;

    private List<Integer> schoolIds;
}