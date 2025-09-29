package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Dto.MonthlyReportDto;
import com.example.Bright_Aid.Entity.MonthlyReport;
import com.example.Bright_Aid.Entity.School;
import com.example.Bright_Aid.Entity.Ngo;
import com.example.Bright_Aid.Entity.Admin;
import com.example.Bright_Aid.repository.MonthlyReportRepository;
import com.example.Bright_Aid.repository.SchoolRepository;
import com.example.Bright_Aid.repository.NgoRepository;
import com.example.Bright_Aid.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MonthlyReportService {

    private final MonthlyReportRepository monthlyReportRepository;
    private final SchoolRepository schoolRepository;
    private final NgoRepository ngoRepository;
    private final AdminRepository adminRepository;

    @Transactional
    public MonthlyReportDto createMonthlyReport(MonthlyReportDto monthlyReportDto) {
        log.info("Creating monthly report for month: {} year: {}", monthlyReportDto.getMonth(), monthlyReportDto.getYear());

        MonthlyReport.MonthlyReportBuilder reportBuilder = MonthlyReport.builder()
                .month(monthlyReportDto.getMonth())
                .year(monthlyReportDto.getYear())
                .totalStudents(monthlyReportDto.getTotalStudents())
                .presentStudents(monthlyReportDto.getPresentStudents())
                .averageAttendance(monthlyReportDto.getAverageAttendance())
                .newEnrollments(monthlyReportDto.getNewEnrollments())
                .dropouts(monthlyReportDto.getDropouts())
                .highRiskStudents(monthlyReportDto.getHighRiskStudents())
                .fundsReceived(monthlyReportDto.getFundsReceived())
                .fundsUtilized(monthlyReportDto.getFundsUtilized())
                .fundsPending(monthlyReportDto.getFundsPending())
                .activeProjects(monthlyReportDto.getActiveProjects())
                .completedProjects(monthlyReportDto.getCompletedProjects())
                .generatedAt(monthlyReportDto.getGeneratedAt() != null ?
                        monthlyReportDto.getGeneratedAt() : LocalDateTime.now());

        // Set School if provided
        if (monthlyReportDto.getSchoolId() != null) {
            School school = schoolRepository.findById(monthlyReportDto.getSchoolId())
                    .orElseThrow(() -> new RuntimeException("School not found with ID: " + monthlyReportDto.getSchoolId()));
            reportBuilder.school(school);
        }

        // Set NGO if provided
        if (monthlyReportDto.getNgoId() != null) {
            Ngo ngo = ngoRepository.findById(monthlyReportDto.getNgoId())
                    .orElseThrow(() -> new RuntimeException("NGO not found with ID: " + monthlyReportDto.getNgoId()));
            reportBuilder.ngo(ngo);
        }

        // Set Admin if provided
        if (monthlyReportDto.getGeneratedBy() != null) {
            Admin admin = adminRepository.findById(monthlyReportDto.getGeneratedBy())
                    .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + monthlyReportDto.getGeneratedBy()));
            reportBuilder.generatedBy(admin);
        }

        MonthlyReport monthlyReport = reportBuilder.build();
        MonthlyReport savedReport = monthlyReportRepository.save(monthlyReport);

        log.info("Successfully created monthly report with ID: {}", savedReport.getReportId());
        return convertToDto(savedReport);
    }

    public MonthlyReportDto getMonthlyReportById(Integer reportId) {
        log.info("Fetching monthly report with ID: {}", reportId);

        MonthlyReport report = monthlyReportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Monthly report not found with ID: " + reportId));

        return convertToDto(report);
    }

    public List<MonthlyReportDto> getAllMonthlyReports() {
        log.info("Fetching all monthly reports");

        List<MonthlyReport> reports = monthlyReportRepository.findAll();
        return reports.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<MonthlyReportDto> getAllMonthlyReports(Pageable pageable) {
        log.info("Fetching monthly reports with pagination: {}", pageable);

        Page<MonthlyReport> reports = monthlyReportRepository.findAll(pageable);
        return reports.map(this::convertToDto);
    }

    @Transactional
    public MonthlyReportDto updateMonthlyReport(Integer reportId, MonthlyReportDto monthlyReportDto) {
        log.info("Updating monthly report with ID: {}", reportId);

        MonthlyReport existingReport = monthlyReportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Monthly report not found with ID: " + reportId));

        // Update School if provided
        if (monthlyReportDto.getSchoolId() != null &&
                (existingReport.getSchool() == null || !monthlyReportDto.getSchoolId().equals(existingReport.getSchool().getSchoolId()))) {
            School school = schoolRepository.findById(monthlyReportDto.getSchoolId())
                    .orElseThrow(() -> new RuntimeException("School not found with ID: " + monthlyReportDto.getSchoolId()));
            existingReport.setSchool(school);
        }

        // Update NGO if provided
        if (monthlyReportDto.getNgoId() != null &&
                (existingReport.getNgo() == null || !monthlyReportDto.getNgoId().equals(existingReport.getNgo().getNgoId()))) {
            Ngo ngo = ngoRepository.findById(monthlyReportDto.getNgoId())
                    .orElseThrow(() -> new RuntimeException("NGO not found with ID: " + monthlyReportDto.getNgoId()));
            existingReport.setNgo(ngo);
        }

        // Update Admin if provided
        if (monthlyReportDto.getGeneratedBy() != null &&
                (existingReport.getGeneratedBy() == null || !monthlyReportDto.getGeneratedBy().equals(existingReport.getGeneratedBy().getAdminId()))) {
            Admin admin = adminRepository.findById(monthlyReportDto.getGeneratedBy())
                    .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + monthlyReportDto.getGeneratedBy()));
            existingReport.setGeneratedBy(admin);
        }

        if (monthlyReportDto.getMonth() != null) {
            existingReport.setMonth(monthlyReportDto.getMonth());
        }
        if (monthlyReportDto.getYear() != null) {
            existingReport.setYear(monthlyReportDto.getYear());
        }
        if (monthlyReportDto.getTotalStudents() != null) {
            existingReport.setTotalStudents(monthlyReportDto.getTotalStudents());
        }
        if (monthlyReportDto.getPresentStudents() != null) {
            existingReport.setPresentStudents(monthlyReportDto.getPresentStudents());
        }
        if (monthlyReportDto.getAverageAttendance() != null) {
            existingReport.setAverageAttendance(monthlyReportDto.getAverageAttendance());
        }
        if (monthlyReportDto.getNewEnrollments() != null) {
            existingReport.setNewEnrollments(monthlyReportDto.getNewEnrollments());
        }
        if (monthlyReportDto.getDropouts() != null) {
            existingReport.setDropouts(monthlyReportDto.getDropouts());
        }
        if (monthlyReportDto.getHighRiskStudents() != null) {
            existingReport.setHighRiskStudents(monthlyReportDto.getHighRiskStudents());
        }
        if (monthlyReportDto.getFundsReceived() != null) {
            existingReport.setFundsReceived(monthlyReportDto.getFundsReceived());
        }
        if (monthlyReportDto.getFundsUtilized() != null) {
            existingReport.setFundsUtilized(monthlyReportDto.getFundsUtilized());
        }
        if (monthlyReportDto.getFundsPending() != null) {
            existingReport.setFundsPending(monthlyReportDto.getFundsPending());
        }
        if (monthlyReportDto.getActiveProjects() != null) {
            existingReport.setActiveProjects(monthlyReportDto.getActiveProjects());
        }
        if (monthlyReportDto.getCompletedProjects() != null) {
            existingReport.setCompletedProjects(monthlyReportDto.getCompletedProjects());
        }
        if (monthlyReportDto.getGeneratedAt() != null) {
            existingReport.setGeneratedAt(monthlyReportDto.getGeneratedAt());
        }

        MonthlyReport updatedReport = monthlyReportRepository.save(existingReport);
        log.info("Successfully updated monthly report with ID: {}", reportId);

        return convertToDto(updatedReport);
    }

    @Transactional
    public void deleteMonthlyReport(Integer reportId) {
        log.info("Deleting monthly report with ID: {}", reportId);

        if (!monthlyReportRepository.existsById(reportId)) {
            throw new RuntimeException("Monthly report not found with ID: " + reportId);
        }

        monthlyReportRepository.deleteById(reportId);
        log.info("Successfully deleted monthly report with ID: {}", reportId);
    }

    public List<MonthlyReportDto> getMonthlyReportsBySchool(Integer schoolId) {
        log.info("Fetching monthly reports for school ID: {}", schoolId);

        List<MonthlyReport> reports = monthlyReportRepository.findAll();
        return reports.stream()
                .filter(report -> report.getSchool() != null && report.getSchool().getSchoolId().equals(schoolId))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<MonthlyReportDto> getMonthlyReportsByNgo(Integer ngoId) {
        log.info("Fetching monthly reports for NGO ID: {}", ngoId);

        List<MonthlyReport> reports = monthlyReportRepository.findAll();
        return reports.stream()
                .filter(report -> report.getNgo() != null && report.getNgo().getNgoId().equals(ngoId))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<MonthlyReportDto> getMonthlyReportsByYear(Integer year) {
        log.info("Fetching monthly reports for year: {}", year);

        List<MonthlyReport> reports = monthlyReportRepository.findAll();
        return reports.stream()
                .filter(report -> report.getYear().equals(year))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<MonthlyReportDto> getMonthlyReportsByMonth(Integer month) {
        log.info("Fetching monthly reports for month: {}", month);

        List<MonthlyReport> reports = monthlyReportRepository.findAll();
        return reports.stream()
                .filter(report -> report.getMonth().equals(month))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<MonthlyReportDto> getMonthlyReportsByMonthAndYear(Integer month, Integer year) {
        log.info("Fetching monthly reports for month: {} and year: {}", month, year);

        List<MonthlyReport> reports = monthlyReportRepository.findAll();
        return reports.stream()
                .filter(report -> report.getMonth().equals(month) && report.getYear().equals(year))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private MonthlyReportDto convertToDto(MonthlyReport report) {
        return MonthlyReportDto.builder()
                .reportId(report.getReportId())
                .schoolId(report.getSchool() != null ? report.getSchool().getSchoolId() : null)
                .ngoId(report.getNgo() != null ? report.getNgo().getNgoId() : null)
                .month(report.getMonth())
                .year(report.getYear())
                .totalStudents(report.getTotalStudents())
                .presentStudents(report.getPresentStudents())
                .averageAttendance(report.getAverageAttendance())
                .newEnrollments(report.getNewEnrollments())
                .dropouts(report.getDropouts())
                .highRiskStudents(report.getHighRiskStudents())
                .fundsReceived(report.getFundsReceived())
                .fundsUtilized(report.getFundsUtilized())
                .fundsPending(report.getFundsPending())
                .activeProjects(report.getActiveProjects())
                .completedProjects(report.getCompletedProjects())
                .generatedAt(report.getGeneratedAt())
                .generatedBy(report.getGeneratedBy() != null ? report.getGeneratedBy().getAdminId() : null)
                .createdAt(report.getCreatedAt())
                .updatedAt(report.getUpdatedAt())
                .build();
    }
}
