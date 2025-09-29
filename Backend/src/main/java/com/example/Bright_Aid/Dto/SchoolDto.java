package com.example.Bright_Aid.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolDto {

    private Integer schoolId;

    @NotNull(message = "User ID is required")
    private Integer userId;

    @NotBlank(message = "School name is required")
    private String schoolName;

    @NotBlank(message = "Registration number is required")
    private String registrationNumber;

    @NotNull(message = "School type is required")
    private String schoolType;

    private String address;

    @NotNull(message = "Division ID is required")
    private Integer divisionId;

    @NotNull(message = "District ID is required")
    private Integer districtId;

    @NotNull(message = "Upazila ID is required")
    private Integer upazilaId;

    private BigDecimal latitude;

    private BigDecimal longitude;

    @Builder.Default
    private String verificationStatus = "PENDING";

    @Builder.Default
    private String status = "ACTIVE";
}