package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Entity.NgoStudentDonations;
import com.example.Bright_Aid.Dto.NgoStudentDonationsDTO;
import com.example.Bright_Aid.repository.NgoStudentDonationsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NgoStudentDonationsService {

    private final NgoStudentDonationsRepository repository;

    public NgoStudentDonationsService(NgoStudentDonationsRepository repository) {
        this.repository = repository;
    }

    // ===================== CREATE =====================
    public NgoStudentDonationsDTO create(NgoStudentDonationsDTO dto) {
        NgoStudentDonations entity = mapToEntity(dto);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        NgoStudentDonations saved = repository.save(entity);
        return mapToDTO(saved);
    }

    // ===================== UPDATE =====================
    public NgoStudentDonationsDTO update(Integer id, NgoStudentDonationsDTO dto) {
        NgoStudentDonations entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donation not found"));

        entity.setAmount(dto.getAmount());
        entity.setDonatedAt(dto.getDonatedAt());
        entity.setDonationType(dto.getDonationType() != null ? parseDonationType(dto.getDonationType()) : null);
        entity.setDonorMessage(dto.getDonorMessage());
        entity.setIsAnonymous(dto.getIsAnonymous());
        entity.setPaymentCompletedAt(dto.getPaymentCompletedAt());
        entity.setPaymentStatus(dto.getPaymentStatus() != null ? parsePaymentStatus(dto.getPaymentStatus()) : null);
        entity.setStudentId(dto.getStudentId());
        entity.setNgoId(dto.getNgoId());
        entity.setTransactionId(dto.getTransactionId());
        entity.setUpdatedAt(LocalDateTime.now());

        NgoStudentDonations updated = repository.save(entity);
        return mapToDTO(updated);
    }

    // ===================== GET BY ID =====================
    public NgoStudentDonationsDTO getById(Integer id) {
        NgoStudentDonations entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donation not found"));
        return mapToDTO(entity);
    }

    // ===================== GET ALL =====================
    public List<NgoStudentDonationsDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ===================== GET BY NGO ID =====================
    public List<NgoStudentDonationsDTO> getByNgoId(Integer ngoId) {
        return repository.findAll()
                .stream()
                .filter(donation -> donation.getNgoId().equals(ngoId))
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ===================== DELETE =====================
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    // ===================== MAPPER METHODS =====================
    private NgoStudentDonationsDTO mapToDTO(NgoStudentDonations entity) {
        return NgoStudentDonationsDTO.builder()
                .studentDonationId(entity.getStudentDonationId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .amount(entity.getAmount())
                .donatedAt(entity.getDonatedAt())
                .donationType(entity.getDonationType() != null ? entity.getDonationType().name() : null)
                .donorMessage(entity.getDonorMessage())
                .isAnonymous(entity.getIsAnonymous())
                .paymentCompletedAt(entity.getPaymentCompletedAt())
                .paymentStatus(entity.getPaymentStatus() != null ? entity.getPaymentStatus().name() : null)
                .studentId(entity.getStudentId())
                .ngoId(entity.getNgoId())
                .transactionId(entity.getTransactionId())
                .build();
    }

    private NgoStudentDonations mapToEntity(NgoStudentDonationsDTO dto) {
        return NgoStudentDonations.builder()
                .amount(dto.getAmount())
                .donatedAt(dto.getDonatedAt())
                .donationType(dto.getDonationType() != null ? parseDonationType(dto.getDonationType()) : null)
                .donorMessage(dto.getDonorMessage())
                .isAnonymous(dto.getIsAnonymous())
                .paymentCompletedAt(dto.getPaymentCompletedAt())
                .paymentStatus(dto.getPaymentStatus() != null ? parsePaymentStatus(dto.getPaymentStatus()) : null)
                .studentId(dto.getStudentId())
                .ngoId(dto.getNgoId())
                .transactionId(dto.getTransactionId())
                .build();
    }
    
    private NgoStudentDonations.PaymentStatus parsePaymentStatus(String status) {
        try {
            return NgoStudentDonations.PaymentStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return NgoStudentDonations.PaymentStatus.PENDING; // Default value
        }
    }
    
    private NgoStudentDonations.DonationType parseDonationType(String type) {
        try {
            return NgoStudentDonations.DonationType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return NgoStudentDonations.DonationType.ONE_TIME; // Default value
        }
    }
}
