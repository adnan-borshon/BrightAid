package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Dto.DonationDto;
import com.example.Bright_Aid.Dto.FundUtilizationDto;
import com.example.Bright_Aid.Dto.SchoolProjectDto;
import com.example.Bright_Aid.Entity.FundUtilization;
import com.example.Bright_Aid.repository.FundUtilizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FundUtilizationService {

    private final FundUtilizationRepository fundUtilizationRepository;
    private final DonationService donationService;
    private final SchoolProjectService schoolProjectService;

    public List<FundUtilizationDto> getFundUtilizationByDonor(Integer donorId) {
        List<FundUtilization> utilizations = fundUtilizationRepository.findByDonorId(donorId);
        return utilizations.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<FundUtilizationDto> getFundUtilizationByNgo(Integer ngoId) {
        List<FundUtilization> utilizations = fundUtilizationRepository.findByNgoId(ngoId);
        return utilizations.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<FundUtilizationDto> getFundUtilizationByProject(Integer projectId) {
        List<FundUtilization> utilizations = fundUtilizationRepository.findByProjectProjectId(projectId);
        return utilizations.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<FundUtilizationDto> getAllFundUtilizations() {
        List<FundUtilization> utilizations = fundUtilizationRepository.findAll();
        return utilizations.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private FundUtilizationDto convertToDto(FundUtilization utilization) {
        FundUtilizationDto dto = FundUtilizationDto.builder()
                .utilizationId(utilization.getUtilizationId())
                .donationId(utilization.getDonation() != null ? utilization.getDonation().getDonationId() : null)
                .projectId(utilization.getProject() != null ? utilization.getProject().getProjectId() : null)
                .amountUsed(utilization.getAmountUsed())
                .specificPurpose(utilization.getSpecificPurpose())
                .detailedDescription(utilization.getDetailedDescription())
                .vendorName(utilization.getVendorName())
                .billInvoiceNumber(utilization.getBillInvoiceNumber())
                .receiptImageUrl(utilization.getReceiptImageUrl())
                .utilizationDate(utilization.getUtilizationDate())
                .utilizationStatus(utilization.getUtilizationStatus())
                .createdAt(utilization.getCreatedAt())
                .updatedAt(utilization.getUpdatedAt())
                .build();

        // Add related entity data
        // Add related entity data safely
        if (utilization.getDonation() != null) {
            try {
                DonationDto donationDto = donationService.getDonationById(utilization.getDonation().getDonationId());
                dto.setDonation(donationDto);
            } catch (Exception e) {
                // Handle case where donation might not be found
            }
        }

        if (utilization.getProject() != null) {
            try {
                SchoolProjectDto projectDto = schoolProjectService.getSchoolProjectById(utilization.getProject().getProjectId());
                dto.setProject(projectDto);
            } catch (Exception e) {
                // Handle case where project might not be found
            }
        }

        return dto;
    }
}