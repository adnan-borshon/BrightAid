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
public class UpazilaDto {

    private Integer upazilaId;

    @NotNull(message = "District ID is required")
    private Integer districtId;

    @NotBlank(message = "Upazila name is required")
    private String upazilaName;

    @NotBlank(message = "Upazila code is required")
    private String upazilaCode;

    private List<Integer> schoolIds;
}