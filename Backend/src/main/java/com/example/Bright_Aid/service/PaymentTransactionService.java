package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Entity.Donation;
import com.example.Bright_Aid.Entity.Donor;
import com.example.Bright_Aid.Entity.PaymentTransaction;
import com.example.Bright_Aid.Dto.PaymentTransactionDto;
import com.example.Bright_Aid.repository.DonationRepository;
import com.example.Bright_Aid.repository.DonorRepository;
import com.example.Bright_Aid.repository.PaymentTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentTransactionService {

    private final PaymentTransactionRepository paymentTransactionRepository;
    private final DonorRepository donorRepository;
    private final DonationRepository donationRepository;
    
    @Value("${sslcommerz.store.id}")
    private String storeId;
    
    @Value("${sslcommerz.store.password}")
    private String storePassword;
    
    @Value("${sslcommerz.sandbox.url}")
    private String sandboxUrl;
    
    @Value("${sslcommerz.success.url}")
    private String successUrl;
    
    @Value("${sslcommerz.fail.url}")
    private String failUrl;
    
    @Value("${sslcommerz.cancel.url}")
    private String cancelUrl;
    
    @Value("${sslcommerz.ipn.url}")
    private String ipnUrl;

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
    
    // SSLCommerz Integration Methods
    public Map<String, Object> initiateSSLCommerzPayment(Integer donorId, BigDecimal amount, 
                                                        String productName, String productCategory) {
        try {
            Donor donor = donorRepository.findById(donorId).orElseThrow();
            String transactionRef = "TXN_" + UUID.randomUUID().toString().substring(0, 8);
            
            // Create transaction record
            PaymentTransaction transaction = PaymentTransaction.builder()
                    .donor(donor)
                    .transactionReference(transactionRef)
                    .amount(amount)
                    .currency("BDT")
                    .transactionType(PaymentTransaction.TransactionType.DONATION)
                    .paymentMethod(PaymentTransaction.PaymentMethod.CARD)
                    .status(PaymentTransaction.TransactionStatus.PENDING)
                    .customerName(donor.getDonorName())
                    .customerEmail(donor.getUser().getEmail())
                    .customerPhone(donor.getUser().getUserProfile() != null ? donor.getUser().getUserProfile().getPhone() : null)
                    .productName(productName)
                    .productCategory(productCategory != null ? productCategory : "Donation")
                    .sessionKey("") // Initialize with empty string to avoid null constraint
                    .initiatedAt(LocalDateTime.now())
                    .build();
            
            PaymentTransaction savedTransaction = paymentTransactionRepository.save(transaction);
            
            // Prepare SSLCommerz request
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("store_id", storeId);
            params.add("store_passwd", storePassword);
            params.add("total_amount", amount.toString());
            params.add("currency", "BDT");
            params.add("tran_id", transactionRef);
            params.add("success_url", successUrl);
            params.add("fail_url", failUrl);
            params.add("cancel_url", cancelUrl);
            params.add("ipn_url", ipnUrl);
            params.add("cus_name", donor.getDonorName());
            params.add("cus_email", donor.getUser().getEmail());
            params.add("cus_add1", "Dhaka");
            params.add("cus_city", "Dhaka");
            params.add("cus_country", "Bangladesh");
            params.add("cus_phone", donor.getUser().getUserProfile() != null && donor.getUser().getUserProfile().getPhone() != null ? donor.getUser().getUserProfile().getPhone() : "01700000000");
            params.add("product_name", productName);
            params.add("product_category", productCategory != null ? productCategory : "Donation");
            params.add("product_profile", "general");
            params.add("shipping_method", "NO");
            params.add("num_of_item", "1");
            params.add("product_amount", amount.toString());
            
            // Call SSLCommerz API
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(sandboxUrl, entity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                String status = (String) responseBody.get("status");
                
                if ("SUCCESS".equals(status)) {
                    String sessionKey = (String) responseBody.get("sessionkey");
                    String gatewayPageURL = (String) responseBody.get("GatewayPageURL");
                    
                    // Update transaction with session key
                    savedTransaction.setSessionKey(sessionKey);
                    paymentTransactionRepository.save(savedTransaction);
                    
                    return Map.of(
                        "status", "SUCCESS",
                        "message", "Payment initiated successfully",
                        "paymentUrl", gatewayPageURL,
                        "transactionId", savedTransaction.getTransactionId(),
                        "transactionReference", transactionRef,
                        "sessionKey", sessionKey
                    );
                } else {
                    // Return SSLCommerz error details
                    String failedReason = (String) responseBody.get("failedreason");
                    return Map.of(
                        "status", "FAILED",
                        "message", "SSLCommerz Error: " + (failedReason != null ? failedReason : "Unknown error"),
                        "transactionId", savedTransaction.getTransactionId(),
                        "sslcommerzResponse", responseBody
                    );
                }
            }
            
            return Map.of(
                "status", "FAILED",
                "message", "No response from SSLCommerz or HTTP error: " + response.getStatusCode(),
                "transactionId", savedTransaction.getTransactionId()
            );
            
        } catch (Exception e) {
            return Map.of(
                "status", "ERROR",
                "message", "Error initiating payment: " + e.getMessage()
            );
        }
    }
    
    public PaymentTransaction getByTransactionReference(String transactionReference) {
        return paymentTransactionRepository.findByTransactionReference(transactionReference).orElse(null);
    }
    
    public void updatePaymentStatus(String transactionReference, String status, Map<String, String> additionalData) {
        PaymentTransaction transaction = paymentTransactionRepository.findByTransactionReference(transactionReference).orElse(null);
        if (transaction != null) {
            switch (status.toUpperCase()) {
                case "VALID":
                case "VALIDATED":
                    transaction.setStatus(PaymentTransaction.TransactionStatus.SUCCESS);
                    transaction.setCompletedAt(LocalDateTime.now());
                    break;
                case "FAILED":
                    transaction.setStatus(PaymentTransaction.TransactionStatus.FAILED);
                    break;
                case "CANCELLED":
                    transaction.setStatus(PaymentTransaction.TransactionStatus.CANCELLED);
                    break;
            }
            
            if (additionalData != null) {
                transaction.setBankTransactionId(additionalData.get("bank_tran_id"));
                transaction.setCardType(additionalData.get("card_type"));
                transaction.setCardNo(additionalData.get("card_no"));
                transaction.setGatewayResponseCode(additionalData.get("status"));
                transaction.setGatewayResponseMessage(additionalData.get("risk_title"));
            }
            
            paymentTransactionRepository.save(transaction);
        }
    }
}