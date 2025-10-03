package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Dto.PaymentCustomerInfoDto;
import com.example.Bright_Aid.Entity.PaymentCustomerInfo;
import com.example.Bright_Aid.Entity.PaymentTransaction;
import com.example.Bright_Aid.repository.PaymentCustomerInfoRepository;
import com.example.Bright_Aid.repository.PaymentTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentCustomerInfoService {

    private final PaymentCustomerInfoRepository repository;
    private final PaymentTransactionRepository transactionRepository;

    // Convert Entity -> DTO
    private PaymentCustomerInfoDto toDto(PaymentCustomerInfo entity) {
        return PaymentCustomerInfoDto.builder()
                .customerInfoId(entity.getCustomerInfoId())
                .transactionId(entity.getTransaction().getTransactionId())
                .customerName(entity.getCustomerName())
                .customerEmail(entity.getCustomerEmail())
                .customerPhone(entity.getCustomerPhone())
                .build();
    }

    // Convert DTO -> Entity
    private PaymentCustomerInfo toEntity(PaymentCustomerInfoDto dto) {
        return PaymentCustomerInfo.builder()
                .customerName(dto.getCustomerName())
                .customerEmail(dto.getCustomerEmail())
                .customerPhone(dto.getCustomerPhone())
                // Transaction will be set outside before saving
                .build();
    }

    public List<PaymentCustomerInfoDto> getAll() {
        return repository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public PaymentCustomerInfoDto getById(Integer id) {
        return repository.findById(id)
                .map(this::toDto)
                .orElse(null);
    }

    public PaymentCustomerInfoDto save(PaymentCustomerInfoDto dto) {
        PaymentCustomerInfo entity = toEntity(dto);
        
        // Set transaction if transactionId is provided
        if (dto.getTransactionId() != null) {
            PaymentTransaction transaction = transactionRepository.findById(dto.getTransactionId())
                    .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + dto.getTransactionId()));
            entity.setTransaction(transaction);
        }
        
        PaymentCustomerInfo saved = repository.save(entity);
        return toDto(saved);
    }

    public PaymentCustomerInfoDto save(PaymentCustomerInfo entity) {
        PaymentCustomerInfo saved = repository.save(entity);
        return toDto(saved);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }
}
