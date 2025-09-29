package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Entity.*;
import com.example.Bright_Aid.Dto.SchoolDocumentDto;
import com.example.Bright_Aid.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SchoolDocumentService {

    private final SchoolDocumentRepository schoolDocumentRepository;
    private final SchoolRepository schoolRepository;
    private final UserRepository userRepository;

    public SchoolDocumentService(SchoolDocumentRepository schoolDocumentRepository,
                                 SchoolRepository schoolRepository,
                                 UserRepository userRepository) {
        this.schoolDocumentRepository = schoolDocumentRepository;
        this.schoolRepository = schoolRepository;
        this.userRepository = userRepository;
    }

    // Create or update SchoolDocument
    public SchoolDocumentDto saveSchoolDocument(SchoolDocumentDto schoolDocumentDto) {
        School school = schoolRepository.findById(schoolDocumentDto.getSchoolId())
                .orElseThrow(() -> new RuntimeException("School not found"));

        User uploadedBy = userRepository.findById(schoolDocumentDto.getUploadedBy())
                .orElseThrow(() -> new RuntimeException("User not found"));

        SchoolDocument schoolDocument = SchoolDocument.builder()
                .documentId(schoolDocumentDto.getDocumentId())
                .school(school)
                .documentType(SchoolDocument.DocumentType.valueOf(schoolDocumentDto.getDocumentType()))
                .documentTitle(schoolDocumentDto.getDocumentTitle())
                .documentDescription(schoolDocumentDto.getDocumentDescription())
                .fileUrl(schoolDocumentDto.getFileUrl())
                .fileHash(schoolDocumentDto.getFileHash())
                .uploadDate(schoolDocumentDto.getUploadDate() != null ?
                        schoolDocumentDto.getUploadDate() : LocalDate.now())
                .uploadedBy(uploadedBy)
                .isCurrent(schoolDocumentDto.getIsCurrent() != null ?
                        schoolDocumentDto.getIsCurrent() : true)
                .build();

        SchoolDocument saved = schoolDocumentRepository.save(schoolDocument);
        return mapToDto(saved);
    }

    // Get all school documents
    public List<SchoolDocumentDto> getAllSchoolDocuments() {
        return schoolDocumentRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Get school document by ID
    public SchoolDocumentDto getSchoolDocumentById(Integer documentId) {
        return schoolDocumentRepository.findById(documentId)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("School document not found"));
    }

    // Delete school document
    public void deleteSchoolDocument(Integer documentId) {
        if (!schoolDocumentRepository.existsById(documentId)) {
            throw new RuntimeException("School document not found");
        }
        schoolDocumentRepository.deleteById(documentId);
    }

    // Update current status
    public SchoolDocumentDto updateCurrentStatus(Integer documentId, Boolean isCurrent) {
        SchoolDocument schoolDocument = schoolDocumentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("School document not found"));

        schoolDocument.setIsCurrent(isCurrent);
        SchoolDocument saved = schoolDocumentRepository.save(schoolDocument);
        return mapToDto(saved);
    }

    // Mark as current
    public SchoolDocumentDto markAsCurrent(Integer documentId) {
        return updateCurrentStatus(documentId, true);
    }

    // Mark as not current
    public SchoolDocumentDto markAsNotCurrent(Integer documentId) {
        return updateCurrentStatus(documentId, false);
    }

    // Map SchoolDocument entity to DTO
    private SchoolDocumentDto mapToDto(SchoolDocument schoolDocument) {
        return SchoolDocumentDto.builder()
                .documentId(schoolDocument.getDocumentId())
                .schoolId(schoolDocument.getSchool().getSchoolId())
                .documentType(schoolDocument.getDocumentType().name())
                .documentTitle(schoolDocument.getDocumentTitle())
                .documentDescription(schoolDocument.getDocumentDescription())
                .fileUrl(schoolDocument.getFileUrl())
                .fileHash(schoolDocument.getFileHash())
                .uploadDate(schoolDocument.getUploadDate())
                .uploadedBy(schoolDocument.getUploadedBy().getUserId())
                .isCurrent(schoolDocument.getIsCurrent())
                .createdAt(schoolDocument.getCreatedAt())
                .updatedAt(schoolDocument.getUpdatedAt())
                .build();
    }
}