package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.NgoProjectSchoolDto;
import com.example.Bright_Aid.service.NgoProjectSchoolService;
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
import java.util.Map;

@RestController
@RequestMapping("/api/ngo-project-schools")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class NgoProjectSchoolController {

    private final NgoProjectSchoolService ngoProjectSchoolService;

    @GetMapping
    public ResponseEntity<List<NgoProjectSchoolDto>> getAllNgoProjectSchools() {
        log.info("GET request received for all NGO project schools");
        try {
            List<NgoProjectSchoolDto> ngoProjectSchools = ngoProjectSchoolService.getAllNgoProjectSchools();
            log.info("Successfully fetched {} NGO project schools", ngoProjectSchools.size());
            return ResponseEntity.ok(ngoProjectSchools);
        } catch (Exception e) {
            log.error("Error fetching all NGO project schools: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<NgoProjectSchoolDto>> getAllNgoProjectSchoolsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "npsId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        log.info("GET request received for paginated NGO project schools - page: {}, size: {}, sortBy: {}, sortDir: {}",
                page, size, sortBy, sortDir);

        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ?
                    Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<NgoProjectSchoolDto> ngoProjectSchoolPage = ngoProjectSchoolService.getAllNgoProjectSchools(pageable);
            log.info("Successfully fetched paginated NGO project schools - total elements: {}, total pages: {}",
                    ngoProjectSchoolPage.getTotalElements(), ngoProjectSchoolPage.getTotalPages());

            return ResponseEntity.ok(ngoProjectSchoolPage);
        } catch (Exception e) {
            log.error("Error fetching paginated NGO project schools: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{npsId}")
    public ResponseEntity<NgoProjectSchoolDto> getNgoProjectSchoolById(@PathVariable Integer npsId) {
        log.info("GET request received for NGO project school with ID: {}", npsId);
        try {
            return ngoProjectSchoolService.getNgoProjectSchoolById(npsId)
                    .map(ngoProjectSchool -> {
                        log.info("Successfully fetched NGO project school with ID: {}", npsId);
                        return ResponseEntity.ok(ngoProjectSchool);
                    })
                    .orElseGet(() -> {
                        log.warn("NGO project school not found with ID: {}", npsId);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            log.error("Error fetching NGO project school with ID {}: {}", npsId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<NgoProjectSchoolDto> createNgoProjectSchool(@Valid @RequestBody NgoProjectSchoolDto ngoProjectSchoolDto) {
        log.info("POST request received to create new NGO project school");
        try {
            NgoProjectSchoolDto createdNgoProjectSchool = ngoProjectSchoolService.createNgoProjectSchool(ngoProjectSchoolDto);
            log.info("Successfully created NGO project school with ID: {}", createdNgoProjectSchool.getNpsId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdNgoProjectSchool);
        } catch (RuntimeException e) {
            log.error("Validation error creating NGO project school: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error creating NGO project school: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{npsId}")
    public ResponseEntity<NgoProjectSchoolDto> updateNgoProjectSchool(@PathVariable Integer npsId,
                                                                      @Valid @RequestBody NgoProjectSchoolDto ngoProjectSchoolDto) {
        log.info("PUT request received to update NGO project school with ID: {}", npsId);
        try {
            return ngoProjectSchoolService.updateNgoProjectSchool(npsId, ngoProjectSchoolDto)
                    .map(updatedNgoProjectSchool -> {
                        log.info("Successfully updated NGO project school with ID: {}", npsId);
                        return ResponseEntity.ok(updatedNgoProjectSchool);
                    })
                    .orElseGet(() -> {
                        log.warn("NGO project school not found for update with ID: {}", npsId);
                        return ResponseEntity.notFound().build();
                    });
        } catch (RuntimeException e) {
            log.error("Validation error updating NGO project school with ID {}: {}", npsId, e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error updating NGO project school with ID {}: {}", npsId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{npsId}")
    public ResponseEntity<Map<String, String>> deleteNgoProjectSchool(@PathVariable Integer npsId) {
        log.info("DELETE request received for NGO project school with ID: {}", npsId);
        try {
            boolean deleted = ngoProjectSchoolService.deleteNgoProjectSchool(npsId);
            if (deleted) {
                log.info("Successfully deleted NGO project school with ID: {}", npsId);
                return ResponseEntity.ok(Map.of("message", "NGO project school deleted successfully"));
            } else {
                log.warn("NGO project school not found for deletion with ID: {}", npsId);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error deleting NGO project school with ID {}: {}", npsId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete NGO project school"));
        }
    }

    @GetMapping("/by-ngo-project/{ngoProjectId}")
    public ResponseEntity<List<NgoProjectSchoolDto>> getNgoProjectSchoolsByNgoProjectId(@PathVariable Integer ngoProjectId) {
        log.info("GET request received for NGO project schools by NGO project ID: {}", ngoProjectId);
        try {
            List<NgoProjectSchoolDto> ngoProjectSchools = ngoProjectSchoolService.getNgoProjectSchoolsByNgoProjectId(ngoProjectId);
            log.info("Successfully fetched {} NGO project schools for NGO project ID: {}", ngoProjectSchools.size(), ngoProjectId);
            return ResponseEntity.ok(ngoProjectSchools);
        } catch (Exception e) {
            log.error("Error fetching NGO project schools by NGO project ID {}: {}", ngoProjectId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/by-school/{schoolId}")
    public ResponseEntity<List<NgoProjectSchoolDto>> getNgoProjectSchoolsBySchoolId(@PathVariable Integer schoolId) {
        log.info("GET request received for NGO project schools by school ID: {}", schoolId);
        try {
            List<NgoProjectSchoolDto> ngoProjectSchools = ngoProjectSchoolService.getNgoProjectSchoolsBySchoolId(schoolId);
            log.info("Successfully fetched {} NGO project schools for school ID: {}", ngoProjectSchools.size(), schoolId);
            return ResponseEntity.ok(ngoProjectSchools);
        } catch (Exception e) {
            log.error("Error fetching NGO project schools by school ID {}: {}", schoolId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/by-participation-status/{participationStatus}")
    public ResponseEntity<List<NgoProjectSchoolDto>> getNgoProjectSchoolsByParticipationStatus(@PathVariable String participationStatus) {
        log.info("GET request received for NGO project schools by participation status: {}", participationStatus);
        try {
            List<NgoProjectSchoolDto> ngoProjectSchools = ngoProjectSchoolService.getNgoProjectSchoolsByParticipationStatus(participationStatus);
            log.info("Successfully fetched {} NGO project schools with participation status: {}", ngoProjectSchools.size(), participationStatus);
            return ResponseEntity.ok(ngoProjectSchools);
        } catch (IllegalArgumentException e) {
            log.error("Invalid participation status: {}", participationStatus);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error fetching NGO project schools by participation status {}: {}", participationStatus, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}