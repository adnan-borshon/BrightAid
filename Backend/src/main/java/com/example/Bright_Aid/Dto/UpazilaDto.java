package com.example.Bright_Aid.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    private String districtName; // Added for better response

    @NotBlank(message = "Upazila name is required")
    @Size(min = 2, max = 100, message = "Upazila name must be between 2 and 100 characters")
    private String upazilaName;

    @NotBlank(message = "Upazila code is required")
    @Size(min = 2, max = 20, message = "Upazila code must be between 2 and 20 characters")
    private String upazilaCode;

    private List<Integer> schoolIds;
}