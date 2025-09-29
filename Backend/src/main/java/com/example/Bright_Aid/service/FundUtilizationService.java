package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Dto.FundUtilizationDto;
import com.example.Bright_Aid.Entity.FundUtilization;
import com.example.Bright_Aid.Entity.Donation;
import com.example.Bright_Aid.Entity.SchoolProject;
import com.example.Bright_Aid.Entity.Admin;
import com.example.Bright_Aid.Entity.FundTransparency;
import com.example.Bright_Aid.repository.FundUtilizationRepository;
import com.example.Bright_Aid.repository.DonationRepository;
import com.example.Bright_Aid.repository.SchoolProjectRepository;
import com.example.Bright_Aid.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FundUtilizationService {

    private final FundUtilizationRepository fundUtilizationRepository;
    private final DonationRepository donationRepository;
    private final SchoolProjectRepository schoolProjectRepository;
    private final AdminRepository adminRepository;

    @Transactional
    public FundUtilizationDto createFundUtilization(FundUtilizationDto fundUtilizationDto) {
        log.info("Creating fund utilization for donation ID: {}", fundUtilizationDto.getDonationId());

        Donation donation = donationRepository.findById(fundUtilizationDto.getDonationId())
                .orElseThrow(() -> new RuntimeException("Donation not found with ID: " + fundUtilizationDto.getDonationId()));

        SchoolProject project = schoolProjectRepository.findById(fundUtilizationDto.getProjectId())
                .orElseThrow(() -> new RuntimeException("School project not found with ID: " + fundUtilizationDto.getProjectId()));

        Admin approvedBy = adminRepository.findById(fundUtilizationDto.getApprovedBy())
                .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + fundUtilizationDto.getApprovedBy()));

        FundUtilization utilization = FundUtilization.builder()
                .donation(donation)
                .project(project)
                .amountUsed(fundUtilizationDto.getAmountUsed())
                .specificPurpose(fundUtilizationDto.getSpecificPurpose())
                .detailedDescription(fundUtilizationDto.getDetailedDescription())
                .vendorName(fundUtilizationDto.getVendorName())
                .billInvoiceNumber(fundUtilizationDto.getBillInvoiceNumber())
                .receiptImageUrl(fundUtilizationDto.getReceiptImageUrl())
                .utilizationDate(fundUtilizationDto.getUtilizationDate())
                .approvedBy(approvedBy)
                .utilizationStatus(fundUtilizationDto.getUtilizationStatus())
                .build();

        FundUtilization savedUtilization = fundUtilizationRepository.save(utilization);
        log.info("Successfully created fund utilization with ID: {}", savedUtilization.getUtilizationId());

        return convertToDto(savedUtilization);
    }

    public FundUtilizationDto getFundUtilizationById(Integer utilizationId) {
        log.info("Fetching fund utilization with ID: {}", utilizationId);

        FundUtilization utilization = fundUtilizationRepository.findById(utilizationId)
                .orElseThrow(() -> new RuntimeException("Fund utilization not found with ID: " + utilizationId));

        return convertToDto(utilization);
    }

    public List<FundUtilizationDto> getAllFundUtilizations() {
        log.info("Fetching all fund utilizations");

        List<FundUtilization> utilizations = fundUtilizationRepository.findAll();
        return utilizations.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<FundUtilizationDto> getAllFundUtilizations(Pageable pageable) {
        log.info("Fetching fund utilizations with pagination: {}", pageable);

        Page<FundUtilization> utilizations = fundUtilizationRepository.findAll(pageable);
        return utilizations.map(this::convertToDto);
    }

    @Transactional
    public FundUtilizationDto updateFundUtilization(Integer utilizationId, FundUtilizationDto fundUtilizationDto) {
        log.info("Updating fund utilization with ID: {}", utilizationId);

        FundUtilization existingUtilization = fundUtilizationRepository.findById(utilizationId)
                .orElseThrow(() -> new RuntimeException("Fund utilization not found with ID: " + utilizationId));

        if (fundUtilizationDto.getDonationId() != null &&
                !fundUtilizationDto.getDonationId().equals(existingUtilization.getDonation().getDonationId())) {
            Donation donation = donationRepository.findById(fundUtilizationDto.getDonationId())
                    .orElseThrow(() -> new RuntimeException("Donation not found with ID: " + fundUtilizationDto.getDonationId()));
            existingUtilization.setDonation(donation);
        }

        if (fundUtilizationDto.getProjectId() != null &&
                !fundUtilizationDto.getProjectId().equals(existingUtilization.getProject().getProjectId())) {
            SchoolProject project = schoolProjectRepository.findById(fundUtilizationDto.getProjectId())
                    .orElseThrow(() -> new RuntimeException("School project not found with ID: " + fundUtilizationDto.getProjectId()));
            existingUtilization.setProject(project);
        }

        if (fundUtilizationDto.getApprovedBy() != null &&
                !fundUtilizationDto.getApprovedBy().equals(existingUtilization.getApprovedBy().getAdminId())) {
            Admin approvedBy = adminRepository.findById(fundUtilizationDto.getApprovedBy())
                    .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + fundUtilizationDto.getApprovedBy()));
            existingUtilization.setApprovedBy(approvedBy);
        }

        if (fundUtilizationDto.getAmountUsed() != null) {
            existingUtilization.setAmountUsed(fundUtilizationDto.getAmountUsed());
        }
        if (fundUtilizationDto.getSpecificPurpose() != null) {
            existingUtilization.setSpecificPurpose(fundUtilizationDto.getSpecificPurpose());
        }
        if (fundUtilizationDto.getDetailedDescription() != null) {
            existingUtilization.setDetailedDescription(fundUtilizationDto.getDetailedDescription());
        }
        if (fundUtilizationDto.getVendorName() != null) {
            existingUtilization.setVendorName(fundUtilizationDto.getVendorName());
        }
        if (fundUtilizationDto.getBillInvoiceNumber() != null) {
            existingUtilization.setBillInvoiceNumber(fundUtilizationDto.getBillInvoiceNumber());
        }
        if (fundUtilizationDto.getReceiptImageUrl() != null) {
            existingUtilization.setReceiptImageUrl(fundUtilizationDto.getReceiptImageUrl());
        }
        if (fundUtilizationDto.getUtilizationDate() != null) {
            existingUtilization.setUtilizationDate(fundUtilizationDto.getUtilizationDate());
        }
        if (fundUtilizationDto.getUtilizationStatus() != null) {
            existingUtilization.setUtilizationStatus(fundUtilizationDto.getUtilizationStatus());
        }

        FundUtilization updatedUtilization = fundUtilizationRepository.save(existingUtilization);
        log.info("Successfully updated fund utilization with ID: {}", utilizationId);

        return convertToDto(updatedUtilization);
    }

    @Transactional
    public void deleteFundUtilization(Integer utilizationId) {
        log.info("Deleting fund utilization with ID: {}", utilizationId);

        if (!fundUtilizationRepository.existsById(utilizationId)) {
            throw new RuntimeException("Fund utilization not found with ID: " + utilizationId);
        }

        fundUtilizationRepository.deleteById(utilizationId);
        log.info("Successfully deleted fund utilization with ID: {}", utilizationId);
    }

    public List<FundUtilizationDto> getFundUtilizationsByStatus(FundUtilization.UtilizationStatus status) {
        log.info("Fetching fund utilizations with status: {}", status);

        List<FundUtilization> utilizations = fundUtilizationRepository.findAll();
        return utilizations.stream()
                .filter(utilization -> utilization.getUtilizationStatus().equals(status))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<FundUtilizationDto> getFundUtilizationsByProject(Integer projectId) {
        log.info("Fetching fund utilizations for project ID: {}", projectId);

        List<FundUtilization> utilizations = fundUtilizationRepository.findAll();
        return utilizations.stream()
                .filter(utilization -> utilization.getProject().getProjectId().equals(projectId))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<FundUtilizationDto> getFundUtilizationsByDonation(Integer donationId) {
        log.info("Fetching fund utilizations for donation ID: {}", donationId);

        List<FundUtilization> utilizations = fundUtilizationRepository.findAll();
        return utilizations.stream()
                .filter(utilization -> utilization.getDonation().getDonationId().equals(donationId))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public FundUtilizationDto updateUtilizationStatus(Integer utilizationId, FundUtilization.UtilizationStatus status) {
        log.info("Updating utilization status for ID: {} to status: {}", utilizationId, status);

        FundUtilization utilization = fundUtilizationRepository.findById(utilizationId)
                .orElseThrow(() -> new RuntimeException("Fund utilization not found with ID: " + utilizationId));

        utilization.setUtilizationStatus(status);
        FundUtilization updatedUtilization = fundUtilizationRepository.save(utilization);

        log.info("Successfully updated utilization status for ID: {}", utilizationId);
        return convertToDto(updatedUtilization);
    }

    private FundUtilizationDto convertToDto(FundUtilization utilization) {
        List<Integer> transparencyRecordIds = null;
        if (utilization.getTransparencyRecords() != null) {
            transparencyRecordIds = utilization.getTransparencyRecords().stream()
                    .map(FundTransparency::getTransparencyId)
                    .collect(Collectors.toList());
        }

        return FundUtilizationDto.builder()
                .utilizationId(utilization.getUtilizationId())
                .donationId(utilization.getDonation().getDonationId())
                .projectId(utilization.getProject().getProjectId())
                .amountUsed(utilization.getAmountUsed())
                .specificPurpose(utilization.getSpecificPurpose())
                .detailedDescription(utilization.getDetailedDescription())
                .vendorName(utilization.getVendorName())
                .billInvoiceNumber(utilization.getBillInvoiceNumber())
                .receiptImageUrl(utilization.getReceiptImageUrl())
                .utilizationDate(utilization.getUtilizationDate())
                .approvedBy(utilization.getApprovedBy().getAdminId())
                .utilizationStatus(utilization.getUtilizationStatus())
                .transparencyRecordIds(transparencyRecordIds)
                .createdAt(utilization.getCreatedAt())
                .updatedAt(utilization.getUpdatedAt())
                .build();
    }
}