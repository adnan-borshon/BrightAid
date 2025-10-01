package com.example.Bright_Aid.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonorDto {

    private Integer donorId;
    private String donorName;
    private String taxId;
    private Boolean isAnonymous;
    private BigDecimal totalDonated;
    private Integer totalSchoolsSupported;
    private Integer totalStudentsSponsored;
    private Integer userId; // reference to User
}
