package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Entity.NgoProjectDonations;
import com.example.Bright_Aid.Dto.NgoProjectDonationsDTO;
import com.example.Bright_Aid.repository.NgoProjectDonationsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NgoProjectDonationsService {

    private final NgoProjectDonationsRepository repository;

    public NgoProjectDonationsService(NgoProjectDonationsRepository repository) {
        this.repository = repository;
    }

    // ===================== CREATE =====================
    public NgoProjectDonationsDTO create(NgoProjectDonationsDTO dto) {
        NgoProjectDonations entity = mapToEntity(dto);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        NgoProjectDonations saved = repository.save(entity);
        return mapToDTO(saved);
    }

    // ===================== UPDATE =====================
    public NgoProjectDonationsDTO update(Integer id, NgoProjectDonationsDTO dto) {
        NgoProjectDonations entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donation not found"));

        entity.setAmount(dto.getAmount());
        entity.setDonatedAt(dto.getDonatedAt());
        entity.setDonationType(dto.getDonationType() != null ? NgoProjectDonations.DonationType.valueOf(dto.getDonationType()) : null);
        entity.setMessage(dto.getMessage());
        entity.setPaymentCompletedAt(dto.getPaymentCompletedAt());
        entity.setPaymentStatus(dto.getPaymentStatus() != null ? NgoProjectDonations.PaymentStatus.valueOf(dto.getPaymentStatus()) : null);
        entity.setProjectId(dto.getProjectId());
        entity.setNgoId(dto.getNgoId());
        entity.setTransactionId(dto.getTransactionId());
        entity.setUpdatedAt(LocalDateTime.now());

        NgoProjectDonations updated = repository.save(entity);
        return mapToDTO(updated);
    }

    // ===================== GET BY ID =====================
    public NgoProjectDonationsDTO getById(Integer id) {
        NgoProjectDonations entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donation not found"));
        return mapToDTO(entity);
    }

    // ===================== GET ALL =====================
    public List<NgoProjectDonationsDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ===================== DELETE =====================
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    // ===================== MAPPER METHODS =====================
    private NgoProjectDonationsDTO mapToDTO(NgoProjectDonations entity) {
        return NgoProjectDonationsDTO.builder()
                .projectDonationId(entity.getProjectDonationId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .amount(entity.getAmount())
                .donatedAt(entity.getDonatedAt())
                .donationType(entity.getDonationType() != null ? entity.getDonationType().name() : null)
                .message(entity.getMessage())
                .paymentCompletedAt(entity.getPaymentCompletedAt())
                .paymentStatus(entity.getPaymentStatus() != null ? entity.getPaymentStatus().name() : null)
                .projectId(entity.getProjectId())
                .ngoId(entity.getNgoId())
                .transactionId(entity.getTransactionId())
                .build();
    }

    private NgoProjectDonations mapToEntity(NgoProjectDonationsDTO dto) {
        return NgoProjectDonations.builder()
                .amount(dto.getAmount())
                .donatedAt(dto.getDonatedAt())
                .donationType(dto.getDonationType() != null ? NgoProjectDonations.DonationType.valueOf(dto.getDonationType()) : null)
                .message(dto.getMessage())
                .paymentCompletedAt(dto.getPaymentCompletedAt())
                .paymentStatus(dto.getPaymentStatus() != null ? NgoProjectDonations.PaymentStatus.valueOf(dto.getPaymentStatus()) : null)
                .projectId(dto.getProjectId())
                .ngoId(dto.getNgoId())
                .transactionId(dto.getTransactionId())
                .build();
    }
}
