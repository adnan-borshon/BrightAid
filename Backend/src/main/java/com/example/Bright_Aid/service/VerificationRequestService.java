package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Dto.VerificationRequestDto;
import com.example.Bright_Aid.Entity.Admin;
import com.example.Bright_Aid.Entity.User;
import com.example.Bright_Aid.Entity.VerificationDocument;
import com.example.Bright_Aid.Entity.VerificationRequest;
import com.example.Bright_Aid.repository.AdminRepository;
import com.example.Bright_Aid.repository.UserRepository;
import com.example.Bright_Aid.repository.VerificationRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VerificationRequestService {

    private final VerificationRequestRepository verificationRequestRepository;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    @Transactional
    public VerificationRequestDto createVerificationRequest(VerificationRequestDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId()));

        Admin reviewedBy = null;
        if (dto.getReviewedBy() != null) {
            reviewedBy = adminRepository.findById(dto.getReviewedBy())
                    .orElseThrow(() -> new RuntimeException("Admin not found with id: " + dto.getReviewedBy()));
        }

        VerificationRequest request = VerificationRequest.builder()
                .user(user)
                .entityType(dto.getEntityType())
                .entityId(dto.getEntityId())
                .documentsUrls(dto.getDocumentsUrls())
                .verificationNotes(dto.getVerificationNotes())
                .adminComments(dto.getAdminComments())
                .status(dto.getStatus())
                .priority(dto.getPriority())
                .reviewedBy(reviewedBy)
                .submittedAt(dto.getSubmittedAt())
                .reviewedAt(dto.getReviewedAt())
                .deadline(dto.getDeadline())
                .build();

        VerificationRequest saved = verificationRequestRepository.save(request);
        return convertToDto(saved);
    }

    @Transactional(readOnly = true)
    public VerificationRequestDto getVerificationRequestById(Integer id) {
        VerificationRequest request = verificationRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("VerificationRequest not found with id: " + id));
        return convertToDto(request);
    }

    @Transactional(readOnly = true)
    public List<VerificationRequestDto> getAllVerificationRequests() {
        return verificationRequestRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public VerificationRequestDto updateVerificationRequest(Integer id, VerificationRequestDto dto) {
        VerificationRequest request = verificationRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("VerificationRequest not found with id: " + id));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId()));

        Admin reviewedBy = null;
        if (dto.getReviewedBy() != null) {
            reviewedBy = adminRepository.findById(dto.getReviewedBy())
                    .orElseThrow(() -> new RuntimeException("Admin not found with id: " + dto.getReviewedBy()));
        }

        request.setUser(user);
        request.setEntityType(dto.getEntityType());
        request.setEntityId(dto.getEntityId());
        request.setDocumentsUrls(dto.getDocumentsUrls());
        request.setVerificationNotes(dto.getVerificationNotes());
        request.setAdminComments(dto.getAdminComments());
        request.setStatus(dto.getStatus());
        request.setPriority(dto.getPriority());
        request.setReviewedBy(reviewedBy);
        request.setSubmittedAt(dto.getSubmittedAt());
        request.setReviewedAt(dto.getReviewedAt());
        request.setDeadline(dto.getDeadline());

        VerificationRequest updated = verificationRequestRepository.save(request);
        return convertToDto(updated);
    }

    @Transactional
    public void deleteVerificationRequest(Integer id) {
        if (!verificationRequestRepository.existsById(id)) {
            throw new RuntimeException("VerificationRequest not found with id: " + id);
        }
        verificationRequestRepository.deleteById(id);
    }

    private VerificationRequestDto convertToDto(VerificationRequest request) {
        Integer reviewedById = null;
        if (request.getReviewedBy() != null) {
            reviewedById = request.getReviewedBy().getAdminId();
        }

        List<Integer> documentIds = null;
        if (request.getDocuments() != null) {
            documentIds = request.getDocuments().stream()
                    .map(VerificationDocument::getDocumentId)
                    .collect(Collectors.toList());
        }

        return VerificationRequestDto.builder()
                .requestId(request.getRequestId())
                .userId(request.getUser().getUserId())
                .entityType(request.getEntityType())
                .entityId(request.getEntityId())
                .documentsUrls(request.getDocumentsUrls())
                .verificationNotes(request.getVerificationNotes())
                .adminComments(request.getAdminComments())
                .status(request.getStatus())
                .priority(request.getPriority())
                .reviewedBy(reviewedById)
                .submittedAt(request.getSubmittedAt())
                .reviewedAt(request.getReviewedAt())
                .deadline(request.getDeadline())
                .documentIds(documentIds)
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .build();
    }
}