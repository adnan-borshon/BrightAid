package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.SchoolDocumentDto;
import com.example.Bright_Aid.service.SchoolDocumentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/school-documents")
public class SchoolDocumentController {

    private final SchoolDocumentService schoolDocumentService;

    public SchoolDocumentController(SchoolDocumentService schoolDocumentService) {
        this.schoolDocumentService = schoolDocumentService;
    }

    // Create new school document
    @PostMapping
    public ResponseEntity<SchoolDocumentDto> createSchoolDocument(@Valid @RequestBody SchoolDocumentDto schoolDocumentDto) {
        SchoolDocumentDto createdDocument = schoolDocumentService.saveSchoolDocument(schoolDocumentDto);
        return new ResponseEntity<>(createdDocument, HttpStatus.CREATED);
    }

    // Get all school documents
    @GetMapping
    public ResponseEntity<List<SchoolDocumentDto>> getAllSchoolDocuments() {
        List<SchoolDocumentDto> documents = schoolDocumentService.getAllSchoolDocuments();
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    // Get school document by ID
    @GetMapping("/{documentId}")
    public ResponseEntity<SchoolDocumentDto> getSchoolDocumentById(@PathVariable Integer documentId) {
        SchoolDocumentDto document = schoolDocumentService.getSchoolDocumentById(documentId);
        return new ResponseEntity<>(document, HttpStatus.OK);
    }

    // Update school document
    @PutMapping("/{documentId}")
    public ResponseEntity<SchoolDocumentDto> updateSchoolDocument(@PathVariable Integer documentId,
                                                                  @Valid @RequestBody SchoolDocumentDto schoolDocumentDto) {
        schoolDocumentDto.setDocumentId(documentId);
        SchoolDocumentDto updatedDocument = schoolDocumentService.saveSchoolDocument(schoolDocumentDto);
        return new ResponseEntity<>(updatedDocument, HttpStatus.OK);
    }

    // Delete school document
    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteSchoolDocument(@PathVariable Integer documentId) {
        schoolDocumentService.deleteSchoolDocument(documentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Update current status
    @PatchMapping("/{documentId}/current-status")
    public ResponseEntity<SchoolDocumentDto> updateCurrentStatus(@PathVariable Integer documentId,
                                                                 @RequestParam Boolean isCurrent) {
        SchoolDocumentDto updatedDocument = schoolDocumentService.updateCurrentStatus(documentId, isCurrent);
        return new ResponseEntity<>(updatedDocument, HttpStatus.OK);
    }

    // Mark as current
    @PatchMapping("/{documentId}/mark-current")
    public ResponseEntity<SchoolDocumentDto> markAsCurrent(@PathVariable Integer documentId) {
        SchoolDocumentDto updatedDocument = schoolDocumentService.markAsCurrent(documentId);
        return new ResponseEntity<>(updatedDocument, HttpStatus.OK);
    }

    // Mark as not current
    @PatchMapping("/{documentId}/mark-not-current")
    public ResponseEntity<SchoolDocumentDto> markAsNotCurrent(@PathVariable Integer documentId) {
        SchoolDocumentDto updatedDocument = schoolDocumentService.markAsNotCurrent(documentId);
        return new ResponseEntity<>(updatedDocument, HttpStatus.OK);
    }
}