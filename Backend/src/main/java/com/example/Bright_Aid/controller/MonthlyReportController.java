package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.MonthlyReportDto;
import com.example.Bright_Aid.service.MonthlyReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monthly-reports")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class MonthlyReportController {

    private final MonthlyReportService monthlyReportService;

    @PostMapping
    public ResponseEntity<MonthlyReportDto> createMonthlyReport(
            @Valid @RequestBody MonthlyReportDto monthlyReportDto) {
        log.info("REST request to create monthly report for month: {} year: {}",
                monthlyReportDto.getMonth(), monthlyReportDto.getYear());

        MonthlyReportDto createdReport = monthlyReportService.createMonthlyReport(monthlyReportDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReport);
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<MonthlyReportDto> getMonthlyReportById(@PathVariable Integer reportId) {
        log.info("REST request to get monthly report with ID: {}", reportId);

        MonthlyReportDto report = monthlyReportService.getMonthlyReportById(reportId);
        return ResponseEntity.ok(report);
    }

    @GetMapping
    public ResponseEntity<List<MonthlyReportDto>> getAllMonthlyReports() {
        log.info("REST request to get all monthly reports");

        List<MonthlyReportDto> reports = monthlyReportService.getAllMonthlyReports();
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<MonthlyReportDto>> getAllMonthlyReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "reportId") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        log.info("REST request to get monthly reports with pagination - page: {}, size: {}, sortBy: {}, sortDirection: {}",
                page, size, sortBy, sortDirection);

        Sort sort = sortDirection.equalsIgnoreCase("DESC") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<MonthlyReportDto> reports = monthlyReportService.getAllMonthlyReports(pageable);

        return ResponseEntity.ok(reports);
    }

    @GetMapping("/by-school/{schoolId}")
    public ResponseEntity<List<MonthlyReportDto>> getMonthlyReportsBySchool(@PathVariable Integer schoolId) {
        log.info("REST request to get monthly reports for school ID: {}", schoolId);

        List<MonthlyReportDto> reports = monthlyReportService.getMonthlyReportsBySchool(schoolId);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/by-ngo/{ngoId}")
    public ResponseEntity<List<MonthlyReportDto>> getMonthlyReportsByNgo(@PathVariable Integer ngoId) {
        log.info("REST request to get monthly reports for NGO ID: {}", ngoId);

        List<MonthlyReportDto> reports = monthlyReportService.getMonthlyReportsByNgo(ngoId);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/by-year/{year}")
    public ResponseEntity<List<MonthlyReportDto>> getMonthlyReportsByYear(@PathVariable Integer year) {
        log.info("REST request to get monthly reports for year: {}", year);

        List<MonthlyReportDto> reports = monthlyReportService.getMonthlyReportsByYear(year);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/by-month/{month}")
    public ResponseEntity<List<MonthlyReportDto>> getMonthlyReportsByMonth(@PathVariable Integer month) {
        log.info("REST request to get monthly reports for month: {}", month);

        List<MonthlyReportDto> reports = monthlyReportService.getMonthlyReportsByMonth(month);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/by-month-year")
    public ResponseEntity<List<MonthlyReportDto>> getMonthlyReportsByMonthAndYear(
            @RequestParam Integer month,
            @RequestParam Integer year) {
        log.info("REST request to get monthly reports for month: {} and year: {}", month, year);

        List<MonthlyReportDto> reports = monthlyReportService.getMonthlyReportsByMonthAndYear(month, year);
        return ResponseEntity.ok(reports);
    }

    @PutMapping("/{reportId}")
    public ResponseEntity<MonthlyReportDto> updateMonthlyReport(
            @PathVariable Integer reportId,
            @Valid @RequestBody MonthlyReportDto monthlyReportDto) {
        log.info("REST request to update monthly report with ID: {}", reportId);

        MonthlyReportDto updatedReport = monthlyReportService.updateMonthlyReport(reportId, monthlyReportDto);
        return ResponseEntity.ok(updatedReport);
    }

    @DeleteMapping("/{reportId}")
    public ResponseEntity<Void> deleteMonthlyReport(@PathVariable Integer reportId) {
        log.info("REST request to delete monthly report with ID: {}", reportId);

        monthlyReportService.deleteMonthlyReport(reportId);
        return ResponseEntity.noContent().build();
    }
}