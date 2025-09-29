package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Dto.PaymentTransactionDto;
import com.example.Bright_Aid.Entity.PaymentTransaction;
import com.example.Bright_Aid.Entity.Donor;
import com.example.Bright_Aid.Entity.Donation;
import com.example.Bright_Aid.repository.PaymentTransactionRepository;
import com.example.Bright_Aid.repository.DonorRepository;
import com.example.Bright_Aid.repository.DonationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PaymentTransactionService {

    private final PaymentTransactionRepository paymentTransactionRepository;
    private final DonorRepository donorRepository;
    private final DonationRepository donationRepository;

    @Transactional
    public PaymentTransactionDto createPaymentTransaction(PaymentTransactionDto paymentTransactionDto) {
        log.info("Creating payment transaction for donor ID: {}", paymentTransactionDto.getDonorId());

        Donor donor = donorRepository.findById(paymentTransactionDto.getDonorId())
                .orElseThrow(() -> new RuntimeException("Donor not found with ID: " + paymentTransactionDto.getDonorId()));

        Donation donation = null;
        if (paymentTransactionDto.getDonationId() != null) {
            donation = donationRepository.findById(paymentTransactionDto.getDonationId())
                    .orElseThrow(() -> new RuntimeException("Donation not found with ID: " + paymentTransactionDto.getDonationId()));
        }

        PaymentTransaction transaction = PaymentTransaction.builder()
                .donor(donor)
                .donation(donation)
                .transactionReference(paymentTransactionDto.getTransactionReference())
                .amount(paymentTransactionDto.getAmount())
                .currency(paymentTransactionDto.getCurrency())
                .transactionType(paymentTransactionDto.getTransactionType())
                .paymentMethod(paymentTransactionDto.getPaymentMethod())
                .status(paymentTransactionDto.getStatus())
                .gatewayResponseCode(paymentTransactionDto.getGatewayResponseCode())
                .gatewayResponseMessage(paymentTransactionDto.getGatewayResponseMessage())
                .customerName(paymentTransactionDto.getCustomerName())
                .customerEmail(paymentTransactionDto.getCustomerEmail())
                .customerPhone(paymentTransactionDto.getCustomerPhone())
                .initiatedAt(paymentTransactionDto.getInitiatedAt())
                .completedAt(paymentTransactionDto.getCompletedAt())
                .build();

        PaymentTransaction savedTransaction = paymentTransactionRepository.save(transaction);
        log.info("Successfully created payment transaction with ID: {}", savedTransaction.getTransactionId());

        return convertToDto(savedTransaction);
    }

    public PaymentTransactionDto getPaymentTransactionById(Integer transactionId) {
        log.info("Fetching payment transaction with ID: {}", transactionId);

        PaymentTransaction transaction = paymentTransactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Payment transaction not found with ID: " + transactionId));

        return convertToDto(transaction);
    }

    public List<PaymentTransactionDto> getAllPaymentTransactions() {
        log.info("Fetching all payment transactions");

        List<PaymentTransaction> transactions = paymentTransactionRepository.findAll();
        return transactions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<PaymentTransactionDto> getAllPaymentTransactions(Pageable pageable) {
        log.info("Fetching payment transactions with pagination: {}", pageable);

        Page<PaymentTransaction> transactions = paymentTransactionRepository.findAll(pageable);
        return transactions.map(this::convertToDto);
    }

    @Transactional
    public PaymentTransactionDto updatePaymentTransaction(Integer transactionId, PaymentTransactionDto paymentTransactionDto) {
        log.info("Updating payment transaction with ID: {}", transactionId);

        PaymentTransaction existingTransaction = paymentTransactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Payment transaction not found with ID: " + transactionId));

        if (paymentTransactionDto.getDonorId() != null &&
                !paymentTransactionDto.getDonorId().equals(existingTransaction.getDonor().getDonorId())) {
            Donor donor = donorRepository.findById(paymentTransactionDto.getDonorId())
                    .orElseThrow(() -> new RuntimeException("Donor not found with ID: " + paymentTransactionDto.getDonorId()));
            existingTransaction.setDonor(donor);
        }

        if (paymentTransactionDto.getDonationId() != null) {
            if (existingTransaction.getDonation() == null ||
                    !paymentTransactionDto.getDonationId().equals(existingTransaction.getDonation().getDonationId())) {
                Donation donation = donationRepository.findById(paymentTransactionDto.getDonationId())
                        .orElseThrow(() -> new RuntimeException("Donation not found with ID: " + paymentTransactionDto.getDonationId()));
                existingTransaction.setDonation(donation);
            }
        }

        if (paymentTransactionDto.getTransactionReference() != null) {
            existingTransaction.setTransactionReference(paymentTransactionDto.getTransactionReference());
        }
        if (paymentTransactionDto.getAmount() != null) {
            existingTransaction.setAmount(paymentTransactionDto.getAmount());
        }
        if (paymentTransactionDto.getCurrency() != null) {
            existingTransaction.setCurrency(paymentTransactionDto.getCurrency());
        }
        if (paymentTransactionDto.getTransactionType() != null) {
            existingTransaction.setTransactionType(paymentTransactionDto.getTransactionType());
        }
        if (paymentTransactionDto.getPaymentMethod() != null) {
            existingTransaction.setPaymentMethod(paymentTransactionDto.getPaymentMethod());
        }
        if (paymentTransactionDto.getStatus() != null) {
            existingTransaction.setStatus(paymentTransactionDto.getStatus());
        }
        if (paymentTransactionDto.getGatewayResponseCode() != null) {
            existingTransaction.setGatewayResponseCode(paymentTransactionDto.getGatewayResponseCode());
        }
        if (paymentTransactionDto.getGatewayResponseMessage() != null) {
            existingTransaction.setGatewayResponseMessage(paymentTransactionDto.getGatewayResponseMessage());
        }
        if (paymentTransactionDto.getCustomerName() != null) {
            existingTransaction.setCustomerName(paymentTransactionDto.getCustomerName());
        }
        if (paymentTransactionDto.getCustomerEmail() != null) {
            existingTransaction.setCustomerEmail(paymentTransactionDto.getCustomerEmail());
        }
        if (paymentTransactionDto.getCustomerPhone() != null) {
            existingTransaction.setCustomerPhone(paymentTransactionDto.getCustomerPhone());
        }
        if (paymentTransactionDto.getInitiatedAt() != null) {
            existingTransaction.setInitiatedAt(paymentTransactionDto.getInitiatedAt());
        }
        if (paymentTransactionDto.getCompletedAt() != null) {
            existingTransaction.setCompletedAt(paymentTransactionDto.getCompletedAt());
        }

        PaymentTransaction updatedTransaction = paymentTransactionRepository.save(existingTransaction);
        log.info("Successfully updated payment transaction with ID: {}", transactionId);

        return convertToDto(updatedTransaction);
    }

    @Transactional
    public void deletePaymentTransaction(Integer transactionId) {
        log.info("Deleting payment transaction with ID: {}", transactionId);

        if (!paymentTransactionRepository.existsById(transactionId)) {
            throw new RuntimeException("Payment transaction not found with ID: " + transactionId);
        }

        paymentTransactionRepository.deleteById(transactionId);
        log.info("Successfully deleted payment transaction with ID: {}", transactionId);
    }

    public List<PaymentTransactionDto> getPaymentTransactionsByStatus(PaymentTransaction.TransactionStatus status) {
        log.info("Fetching payment transactions with status: {}", status);

        List<PaymentTransaction> transactions = paymentTransactionRepository.findAll();
        return transactions.stream()
                .filter(transaction -> transaction.getStatus().equals(status))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PaymentTransactionDto> getPaymentTransactionsByDonor(Integer donorId) {
        log.info("Fetching payment transactions for donor ID: {}", donorId);

        List<PaymentTransaction> transactions = paymentTransactionRepository.findAll();
        return transactions.stream()
                .filter(transaction -> transaction.getDonor().getDonorId().equals(donorId))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PaymentTransactionDto> getPaymentTransactionsByType(PaymentTransaction.TransactionType type) {
        log.info("Fetching payment transactions with type: {}", type);

        List<PaymentTransaction> transactions = paymentTransactionRepository.findAll();
        return transactions.stream()
                .filter(transaction -> transaction.getTransactionType().equals(type))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PaymentTransactionDto> getPaymentTransactionsByPaymentMethod(PaymentTransaction.PaymentMethod method) {
        log.info("Fetching payment transactions with payment method: {}", method);

        List<PaymentTransaction> transactions = paymentTransactionRepository.findAll();
        return transactions.stream()
                .filter(transaction -> transaction.getPaymentMethod().equals(method))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public PaymentTransactionDto updateTransactionStatus(Integer transactionId, PaymentTransaction.TransactionStatus status) {
        log.info("Updating transaction status for ID: {} to status: {}", transactionId, status);

        PaymentTransaction transaction = paymentTransactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Payment transaction not found with ID: " + transactionId));

        transaction.setStatus(status);

        // Set completed time if transaction is successful
        if (status == PaymentTransaction.TransactionStatus.SUCCESS && transaction.getCompletedAt() == null) {
            transaction.setCompletedAt(LocalDateTime.now());
        }

        PaymentTransaction updatedTransaction = paymentTransactionRepository.save(transaction);

        log.info("Successfully updated transaction status for ID: {}", transactionId);
        return convertToDto(updatedTransaction);
    }

    @Transactional
    public PaymentTransactionDto initiateTransaction(Integer transactionId) {
        log.info("Initiating payment transaction with ID: {}", transactionId);

        PaymentTransaction transaction = paymentTransactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Payment transaction not found with ID: " + transactionId));

        transaction.setStatus(PaymentTransaction.TransactionStatus.PROCESSING);
        transaction.setInitiatedAt(LocalDateTime.now());

        PaymentTransaction updatedTransaction = paymentTransactionRepository.save(transaction);
        log.info("Successfully initiated payment transaction with ID: {}", transactionId);

        return convertToDto(updatedTransaction);
    }

    private PaymentTransactionDto convertToDto(PaymentTransaction transaction) {
        return PaymentTransactionDto.builder()
                .transactionId(transaction.getTransactionId())
                .donorId(transaction.getDonor().getDonorId())
                .donorName(transaction.getDonor().getUser() != null ? transaction.getDonor().getUser().getUsername():transaction.getDonor().getOrganizationName())
                .donorEmail(transaction.getDonor().getUser() != null ? transaction.getDonor().getUser().getEmail() : null)
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
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .build();
    }
}