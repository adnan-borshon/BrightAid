package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Entity.Donation;
import com.example.Bright_Aid.Entity.Donor;
import com.example.Bright_Aid.Entity.PaymentTransaction;
import com.example.Bright_Aid.Dto.PaymentTransactionDto;
import com.example.Bright_Aid.repository.DonationRepository;
import com.example.Bright_Aid.repository.DonorRepository;
import com.example.Bright_Aid.repository.PaymentTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentTransactionService {

    private final PaymentTransactionRepository paymentTransactionRepository;
    private final DonorRepository donorRepository;
    private final DonationRepository donationRepository;

    // Create
    public PaymentTransactionDto create(PaymentTransactionDto dto) {
        Donor donor = donorRepository.findById(dto.getDonorId()).orElseThrow();
        Donation donation = dto.getDonationId() != null
                ? donationRepository.findById(dto.getDonationId()).orElse(null)
                : null;

        PaymentTransaction transaction = PaymentTransaction.builder()
                .donor(donor)
                .donation(donation)
                .transactionReference(dto.getTransactionReference())
                .amount(dto.getAmount())
                .currency(dto.getCurrency())
                .transactionType(dto.getTransactionType())
                .paymentMethod(dto.getPaymentMethod())
                .status(dto.getStatus())
                .gatewayResponseCode(dto.getGatewayResponseCode())
                .gatewayResponseMessage(dto.getGatewayResponseMessage())
                .customerName(dto.getCustomerName())
                .customerEmail(dto.getCustomerEmail())
                .customerPhone(dto.getCustomerPhone())
                .initiatedAt(dto.getInitiatedAt())
                .completedAt(dto.getCompletedAt())
                .build();

        return toDTO(paymentTransactionRepository.save(transaction));
    }

    // Get all
    public List<PaymentTransactionDto> getAll() {
        return paymentTransactionRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Get by ID
    public PaymentTransactionDto getById(Integer id) {
        return paymentTransactionRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow();
    }

    // Update
    public PaymentTransactionDto update(Integer id, PaymentTransactionDto dto) {
        PaymentTransaction transaction = paymentTransactionRepository.findById(id).orElseThrow();

        transaction.setTransactionReference(dto.getTransactionReference());
        transaction.setAmount(dto.getAmount());
        transaction.setCurrency(dto.getCurrency());
        transaction.setTransactionType(dto.getTransactionType());
        transaction.setPaymentMethod(dto.getPaymentMethod());
        transaction.setStatus(dto.getStatus());
        transaction.setGatewayResponseCode(dto.getGatewayResponseCode());
        transaction.setGatewayResponseMessage(dto.getGatewayResponseMessage());
        transaction.setCustomerName(dto.getCustomerName());
        transaction.setCustomerEmail(dto.getCustomerEmail());
        transaction.setCustomerPhone(dto.getCustomerPhone());
        transaction.setInitiatedAt(dto.getInitiatedAt());
        transaction.setCompletedAt(dto.getCompletedAt());

        return toDTO(paymentTransactionRepository.save(transaction));
    }

    // Delete
    public void delete(Integer id) {
        paymentTransactionRepository.deleteById(id);
    }

    // Mapper
    private PaymentTransactionDto toDTO(PaymentTransaction transaction) {
        return PaymentTransactionDto.builder()
                .transactionId(transaction.getTransactionId())
                .donorId(transaction.getDonor().getDonorId())
                .donationId(transaction.getDonation() != null ? transaction.getDonation().getDonationId() : null)
                .transactionReference(transaction.getTransactionReference())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .transactionType(transaction.getTransactionType())
                .paymentMethod(transaction.getPaymentMethod())
                .status(transaction.getStatus())
                .gatewayResponseCode(transaction.getGatewayResponseCode())
                .gatewayResponseMessage(transaction.getGatewayResponseMessage())
                .customerName(transaction.getCustomerName())
                .customerEmail(transaction.getCustomerEmail())
                .customerPhone(transaction.getCustomerPhone())
                .initiatedAt(transaction.getInitiatedAt())
                .completedAt(transaction.getCompletedAt())
                .build();
    }
}
