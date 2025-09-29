package com.example.Bright_Aid.Dto;

import com.example.Bright_Aid.Entity.Ngo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NgoDto {

    private Integer ngoId;

    private Integer ngoProjectId;

    @NotNull(message = "User ID is required")
    private Integer userId;

    @NotBlank(message = "NGO name is required")
    private String ngoName;

    @NotBlank(message = "Registration number is required")
    private String registrationNumber;

    private String description;

    private String contactPerson;

    private String contactPhone;

    @Builder.Default
    private Ngo.VerificationStatus verificationStatus = Ngo.VerificationStatus.PENDING;

    private LocalDateTime verifiedAt;

    private Integer verifiedBy;

    private List<Integer> projectIds;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
