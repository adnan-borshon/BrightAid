package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Dto.VerificationDocumentDto;
import com.example.Bright_Aid.Entity.Admin;
import com.example.Bright_Aid.Entity.VerificationDocument;
import com.example.Bright_Aid.Entity.VerificationRequest;
import com.example.Bright_Aid.repository.AdminRepository;
import com.example.Bright_Aid.repository.VerificationDocumentRepository;
import com.example.Bright_Aid.repository.VerificationRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VerificationDocumentService {

    private final VerificationDocumentRepository verificationDocumentRepository;
    private final VerificationRequestRepository verificationRequestRepository;
    private final AdminRepository adminRepository;

    @Transactional
    public VerificationDocumentDto createVerificationDocument(VerificationDocumentDto dto) {
        VerificationRequest request = verificationRequestRepository.findById(dto.getRequestId())
                .orElseThrow(() -> new RuntimeException("VerificationRequest not found with id: " + dto.getRequestId()));

        Admin admin = adminRepository.findById(dto.getAdminId())
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + dto.getAdminId()));

        VerificationDocument document = VerificationDocument.builder()
                .request(request)
                .documentType(dto.getDocumentType())
                .documentName(dto.getDocumentName())
                .fileUrl(dto.getFileUrl())
                .fileHash(dto.getFileHash())
                .isVerified(dto.getIsVerified())
                .admin(admin)
                .uploadedAt(LocalDateTime.now())
                .verifiedAt(dto.getVerifiedAt())
                .build();

        VerificationDocument saved = verificationDocumentRepository.save(document);
        return convertToDto(saved);
    }

    @Transactional(readOnly = true)
    public VerificationDocumentDto getVerificationDocumentById(Integer id) {
        VerificationDocument document = verificationDocumentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("VerificationDocument not found with id: " + id));
        return convertToDto(document);
    }

    @Transactional(readOnly = true)
    public List<VerificationDocumentDto> getAllVerificationDocuments() {
        return verificationDocumentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public VerificationDocumentDto updateVerificationDocument(Integer id, VerificationDocumentDto dto) {
        VerificationDocument document = verificationDocumentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("VerificationDocument not found with id: " + id));

        VerificationRequest request = verificationRequestRepository.findById(dto.getRequestId())
                .orElseThrow(() -> new RuntimeException("VerificationRequest not found with id: " + dto.getRequestId()));

        Admin admin = adminRepository.findById(dto.getAdminId())
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + dto.getAdminId()));

        document.setRequest(request);
        document.setDocumentType(dto.getDocumentType());
        document.setDocumentName(dto.getDocumentName());
        document.setFileUrl(dto.getFileUrl());
        document.setFileHash(dto.getFileHash());
        document.setIsVerified(dto.getIsVerified());
        document.setAdmin(admin);
        document.setVerifiedAt(dto.getVerifiedAt());

        VerificationDocument updated = verificationDocumentRepository.save(document);
        return convertToDto(updated);
    }

    @Transactional
    public void deleteVerificationDocument(Integer id) {
        if (!verificationDocumentRepository.existsById(id)) {
            throw new RuntimeException("VerificationDocument not found with id: " + id);
        }
        verificationDocumentRepository.deleteById(id);
    }

    private VerificationDocumentDto convertToDto(VerificationDocument document) {
        return VerificationDocumentDto.builder()
                .documentId(document.getDocumentId())
                .requestId(document.getRequest().getRequestId())
                .documentType(document.getDocumentType())
                .documentName(document.getDocumentName())
                .fileUrl(document.getFileUrl())
                .fileHash(document.getFileHash())
                .isVerified(document.getIsVerified())
                .adminId(document.getAdmin().getAdminId())
                .uploadedAt(document.getUploadedAt())
                .verifiedAt(document.getVerifiedAt())
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .build();
    }
}