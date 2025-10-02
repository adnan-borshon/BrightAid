package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Entity.Donation;
import com.example.Bright_Aid.Entity.Donor;
import com.example.Bright_Aid.Entity.DonorGamification;
import com.example.Bright_Aid.Entity.PaymentTransaction;
import com.example.Bright_Aid.Dto.PaymentTransactionDto;
import com.example.Bright_Aid.repository.DonationRepository;
import com.example.Bright_Aid.repository.DonorGamificationRepository;
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
    private final DonorGamificationRepository gamificationRepository;
    
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
            System.out.println("Initiating payment for donorId: " + donorId);
            
            Donor donor = donorRepository.findById(donorId)
                    .orElseThrow(() -> new RuntimeException("Donor not found with ID: " + donorId));
            String transactionRef = "TXN_" + UUID.randomUUID().toString().substring(0, 8);
            System.out.println("Generated transaction reference: " + transactionRef);
            System.out.println("Donor found: " + donor.getDonorName());
            
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
            System.out.println("Transaction saved with ID: " + savedTransaction.getTransactionId());
            
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
            System.err.println("Payment initiation error: " + e.getMessage());
            e.printStackTrace();
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
                    // Create donation record for successful payment
                    createDonationFromTransaction(transaction);
                    // Award points for successful payment
                    awardPointsForDonation(transaction.getDonor(), transaction.getAmount());
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
    
    // Automatic Point System: 1 BDT = 5 Points (৳100 = 500 points)
    private void awardPointsForDonation(Donor donor, BigDecimal amount) {
        try {
            // Convert to int first, then multiply by 5 for exact calculation
            int pointsToAward = amount.intValue() * 5;
            
            DonorGamification gamification = gamificationRepository.findByDonor(donor)
                    .orElse(DonorGamification.builder()
                            .donor(donor)
                            .totalPoints(0)
                            .currentLevel("Bronze")
                            .lastUpdated(LocalDateTime.now())
                            .build());
            
            // Add new points
            gamification.setTotalPoints(gamification.getTotalPoints() + pointsToAward);
            
            // Update level based on total points
            gamification.setCurrentLevel(calculateLevel(gamification.getTotalPoints()));
            
            // Update badges
            List<String> badges = calculateBadges(gamification.getTotalPoints());
            gamification.setBadgesEarned(badges);
            
            gamification.setLastUpdated(LocalDateTime.now());
            
            gamificationRepository.save(gamification);
            
        } catch (Exception e) {
            // Log error but don't fail payment
            System.err.println("Error awarding points: " + e.getMessage());
        }
    }
    
    private String calculateLevel(int totalPoints) {
        if (totalPoints >= 50000) return "Diamond";      // ৳10,000+
        if (totalPoints >= 25000) return "Platinum";     // ৳5,000+
        if (totalPoints >= 10000) return "Gold";         // ৳2,000+
        if (totalPoints >= 2500) return "Silver";        // ৳500+
        return "Bronze";                                  // < ৳500
    }
    
    private List<String> calculateBadges(int totalPoints) {
        List<String> badges = new java.util.ArrayList<>();
        
        if (totalPoints >= 500) badges.add("First Donor");        // ৳100
        if (totalPoints >= 2500) badges.add("Generous Heart");    // ৳500
        if (totalPoints >= 5000) badges.add("Education Champion"); // ৳1,000
        if (totalPoints >= 10000) badges.add("School Builder");   // ৳2,000
        if (totalPoints >= 25000) badges.add("Community Hero");   // ৳5,000
        if (totalPoints >= 50000) badges.add("BrightAid Legend"); // ৳10,000
        
        return badges;
    }
    
    // Create donation record from successful payment transaction
    private void createDonationFromTransaction(PaymentTransaction transaction) {
        try {
            Donation donation = Donation.builder()
                    .donor(transaction.getDonor())
                    .amount(transaction.getAmount())
                    .donationType(mapTransactionTypeToDonationType(transaction.getTransactionType()))
                    .purpose(mapTransactionTypeToPurpose(transaction.getTransactionType()))
                    .paymentStatus(Donation.PaymentStatus.COMPLETED)
                    .donorMessage("Payment via " + transaction.getPaymentMethod())
                    .isAnonymous(false)
                    .donatedAt(LocalDateTime.now())
                    .paymentCompletedAt(LocalDateTime.now())
                    .build();
            
            Donation savedDonation = donationRepository.save(donation);
            
            // Link the donation to the transaction
            transaction.setDonation(savedDonation);
            paymentTransactionRepository.save(transaction);
            
        } catch (Exception e) {
            System.err.println("Error creating donation from transaction: " + e.getMessage());
        }
    }
    
    private Donation.DonationType mapTransactionTypeToDonationType(PaymentTransaction.TransactionType transactionType) {
        // All payments are one-time donations for now
        return Donation.DonationType.ONE_TIME;
    }
    
    private Donation.DonationPurpose mapTransactionTypeToPurpose(PaymentTransaction.TransactionType transactionType) {
        switch (transactionType) {
            case SPONSORSHIP:
                return Donation.DonationPurpose.STUDENT_SPONSORSHIP;
            case DONATION:
            default:
                return Donation.DonationPurpose.GENERAL_SUPPORT;
        }
    }
}