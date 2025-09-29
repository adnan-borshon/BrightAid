package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.FinancialReportDto;
import com.example.Bright_Aid.service.FinancialReportService;
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
@RequestMapping("/api/financial-reports")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class FinancialReportController {

    private final FinancialReportService financialReportService;

    @PostMapping
    public ResponseEntity<FinancialReportDto> createFinancialReport(
            @Valid @RequestBody FinancialReportDto financialReportDto) {
        log.info("REST request to create financial report of type: {}", financialReportDto.getReportType());

        FinancialReportDto createdReport = financialReportService.createFinancialReport(financialReportDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReport);
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<FinancialReportDto> getFinancialReportById(@PathVariable Integer reportId) {
        log.info("REST request to get financial report with ID: {}", reportId);

        FinancialReportDto report = financialReportService.getFinancialReportById(reportId);
        return ResponseEntity.ok(report);
    }

    @GetMapping
    public ResponseEntity<List<FinancialReportDto>> getAllFinancialReports() {
        log.info("REST request to get all financial reports");

        List<FinancialReportDto> reports = financialReportService.getAllFinancialReports();
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<FinancialReportDto>> getAllFinancialReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "reportId") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        log.info("REST request to get financial reports with pagination - page: {}, size: {}, sortBy: {}, sortDirection: {}",
                page, size, sortBy, sortDirection);

        Sort sort = sortDirection.equalsIgnoreCase("DESC") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<FinancialReportDto> reports = financialReportService.getAllFinancialReports(pageable);

        return ResponseEntity.ok(reports);
    }

    @PutMapping("/{reportId}")
    public ResponseEntity<FinancialReportDto> updateFinancialReport(
            @PathVariable Integer reportId,
            @Valid @RequestBody FinancialReportDto financialReportDto) {
        log.info("REST request to update financial report with ID: {}", reportId);

        FinancialReportDto updatedReport = financialReportService.updateFinancialReport(reportId, financialReportDto);
        return ResponseEntity.ok(updatedReport);
    }

    @DeleteMapping("/{reportId}")
    public ResponseEntity<Void> deleteFinancialReport(@PathVariable Integer reportId) {
        log.info("REST request to delete financial report with ID: {}", reportId);

        financialReportService.deleteFinancialReport(reportId);
        return ResponseEntity.noContent().build();
    }
}