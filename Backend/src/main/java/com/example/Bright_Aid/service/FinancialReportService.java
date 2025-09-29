package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Dto.FinancialReportDto;
import com.example.Bright_Aid.Entity.*;
import com.example.Bright_Aid.repository.*;
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
public class FinancialReportService {

    private final FinancialReportRepository financialReportRepository;
    private final SchoolRepository schoolRepository;
    private final NgoRepository ngoRepository;
    private final DonorRepository donorRepository;
    private final AdminRepository adminRepository;

    @Transactional
    public FinancialReportDto createFinancialReport(FinancialReportDto financialReportDto) {
        log.info("Creating financial report for type: {}", financialReportDto.getReportType());

        FinancialReport.FinancialReportBuilder reportBuilder = FinancialReport.builder()
                .reportType(financialReportDto.getReportType())
                .reportPeriodStart(financialReportDto.getReportPeriodStart())
                .reportPeriodEnd(financialReportDto.getReportPeriodEnd())
                .reportData(financialReportDto.getReportData())
                .totalDonations(financialReportDto.getTotalDonations())
                .totalUtilized(financialReportDto.getTotalUtilized())
                .pendingAmount(financialReportDto.getPendingAmount())
                .transparencyScore(financialReportDto.getTransparencyScore())
                .reportFileUrl(financialReportDto.getReportFileUrl());

        // Set School if provided
        if (financialReportDto.getSchoolId() != null) {
            School school = schoolRepository.findById(financialReportDto.getSchoolId())
                    .orElseThrow(() -> new RuntimeException("School not found with ID: " + financialReportDto.getSchoolId()));
            reportBuilder.school(school);
        }

        // Set NGO if provided
        if (financialReportDto.getNgoId() != null) {
            Ngo ngo = ngoRepository.findById(financialReportDto.getNgoId())
                    .orElseThrow(() -> new RuntimeException("NGO not found with ID: " + financialReportDto.getNgoId()));
            reportBuilder.ngo(ngo);
        }

        // Set Donor if provided
        if (financialReportDto.getDonorId() != null) {
            Donor donor = donorRepository.findById(financialReportDto.getDonorId())
                    .orElseThrow(() -> new RuntimeException("Donor not found with ID: " + financialReportDto.getDonorId()));
            reportBuilder.donor(donor);
        }

        // Set Admin if provided
        if (financialReportDto.getGeneratedBy() != null) {
            Admin admin = adminRepository.findById(financialReportDto.getGeneratedBy())
                    .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + financialReportDto.getGeneratedBy()));
            reportBuilder.generatedBy(admin);
        }

        FinancialReport financialReport = reportBuilder.build();
        FinancialReport savedReport = financialReportRepository.save(financialReport);

        log.info("Successfully created financial report with ID: {}", savedReport.getReportId());
        return convertToDto(savedReport);
    }

    public FinancialReportDto getFinancialReportById(Integer reportId) {
        log.info("Fetching financial report with ID: {}", reportId);

        FinancialReport report = financialReportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Financial report not found with ID: " + reportId));

        return convertToDto(report);
    }

    public List<FinancialReportDto> getAllFinancialReports() {
        log.info("Fetching all financial reports");

        List<FinancialReport> reports = financialReportRepository.findAll();
        return reports.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<FinancialReportDto> getAllFinancialReports(Pageable pageable) {
        log.info("Fetching financial reports with pagination: {}", pageable);

        Page<FinancialReport> reports = financialReportRepository.findAll(pageable);
        return reports.map(this::convertToDto);
    }

    @Transactional
    public FinancialReportDto updateFinancialReport(Integer reportId, FinancialReportDto financialReportDto) {
        log.info("Updating financial report with ID: {}", reportId);

        FinancialReport existingReport = financialReportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Financial report not found with ID: " + reportId));

        if (financialReportDto.getReportType() != null) {
            existingReport.setReportType(financialReportDto.getReportType());
        }
        if (financialReportDto.getReportPeriodStart() != null) {
            existingReport.setReportPeriodStart(financialReportDto.getReportPeriodStart());
        }
        if (financialReportDto.getReportPeriodEnd() != null) {
            existingReport.setReportPeriodEnd(financialReportDto.getReportPeriodEnd());
        }

        // Update School if provided
        if (financialReportDto.getSchoolId() != null &&
                (existingReport.getSchool() == null || !financialReportDto.getSchoolId().equals(existingReport.getSchool().getSchoolId()))) {
            School school = schoolRepository.findById(financialReportDto.getSchoolId())
                    .orElseThrow(() -> new RuntimeException("School not found with ID: " + financialReportDto.getSchoolId()));
            existingReport.setSchool(school);
        }

        // Update NGO if provided
        if (financialReportDto.getNgoId() != null &&
                (existingReport.getNgo() == null || !financialReportDto.getNgoId().equals(existingReport.getNgo().getNgoId()))) {
            Ngo ngo = ngoRepository.findById(financialReportDto.getNgoId())
                    .orElseThrow(() -> new RuntimeException("NGO not found with ID: " + financialReportDto.getNgoId()));
            existingReport.setNgo(ngo);
        }

        // Update Donor if provided
        if (financialReportDto.getDonorId() != null &&
                (existingReport.getDonor() == null || !financialReportDto.getDonorId().equals(existingReport.getDonor().getDonorId()))) {
            Donor donor = donorRepository.findById(financialReportDto.getDonorId())
                    .orElseThrow(() -> new RuntimeException("Donor not found with ID: " + financialReportDto.getDonorId()));
            existingReport.setDonor(donor);
        }

        // Update Admin if provided
        if (financialReportDto.getGeneratedBy() != null &&
                (existingReport.getGeneratedBy() == null || !financialReportDto.getGeneratedBy().equals(existingReport.getGeneratedBy().getAdminId()))) {
            Admin admin = adminRepository.findById(financialReportDto.getGeneratedBy())
                    .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + financialReportDto.getGeneratedBy()));
            existingReport.setGeneratedBy(admin);
        }

        if (financialReportDto.getReportData() != null) {
            existingReport.setReportData(financialReportDto.getReportData());
        }
        if (financialReportDto.getTotalDonations() != null) {
            existingReport.setTotalDonations(financialReportDto.getTotalDonations());
        }
        if (financialReportDto.getTotalUtilized() != null) {
            existingReport.setTotalUtilized(financialReportDto.getTotalUtilized());
        }
        if (financialReportDto.getPendingAmount() != null) {
            existingReport.setPendingAmount(financialReportDto.getPendingAmount());
        }
        if (financialReportDto.getTransparencyScore() != null) {
            existingReport.setTransparencyScore(financialReportDto.getTransparencyScore());
        }
        if (financialReportDto.getReportFileUrl() != null) {
            existingReport.setReportFileUrl(financialReportDto.getReportFileUrl());
        }

        FinancialReport updatedReport = financialReportRepository.save(existingReport);
        log.info("Successfully updated financial report with ID: {}", reportId);

        return convertToDto(updatedReport);
    }

    @Transactional
    public void deleteFinancialReport(Integer reportId) {
        log.info("Deleting financial report with ID: {}", reportId);

        if (!financialReportRepository.existsById(reportId)) {
            throw new RuntimeException("Financial report not found with ID: " + reportId);
        }

        financialReportRepository.deleteById(reportId);
        log.info("Successfully deleted financial report with ID: {}", reportId);
    }

    private FinancialReportDto convertToDto(FinancialReport report) {
        return FinancialReportDto.builder()
                .reportId(report.getReportId())
                .reportType(report.getReportType())
                .reportPeriodStart(report.getReportPeriodStart())
                .reportPeriodEnd(report.getReportPeriodEnd())
                .schoolId(report.getSchool() != null ? report.getSchool().getSchoolId() : null)
                .ngoId(report.getNgo() != null ? report.getNgo().getNgoId() : null)
                .donorId(report.getDonor() != null ? report.getDonor().getDonorId() : null)
                .reportData(report.getReportData())
                .totalDonations(report.getTotalDonations())
                .totalUtilized(report.getTotalUtilized())
                .pendingAmount(report.getPendingAmount())
                .transparencyScore(report.getTransparencyScore())
                .generatedBy(report.getGeneratedBy() != null ? report.getGeneratedBy().getAdminId() : null)
                .reportFileUrl(report.getReportFileUrl())
                .generatedAt(report.getGeneratedAt())
                .build();
    }
}
