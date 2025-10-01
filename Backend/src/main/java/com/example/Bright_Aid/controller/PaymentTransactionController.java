package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.PaymentTransactionDto;
import com.example.Bright_Aid.Entity.PaymentTransaction;
import com.example.Bright_Aid.service.PaymentTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payment-transactions")
@RequiredArgsConstructor
@Tag(name = "Payment Transactions", description = "Payment transaction management with SSLCommerz integration")
public class PaymentTransactionController {

    private final PaymentTransactionService paymentTransactionService;

    // Create
    @PostMapping
    public ResponseEntity<PaymentTransactionDto> create(@RequestBody PaymentTransactionDto dto) {
        return ResponseEntity.ok(paymentTransactionService.create(dto));
    }

    // Get all
    @GetMapping
    public ResponseEntity<List<PaymentTransactionDto>> getAll() {
        return ResponseEntity.ok(paymentTransactionService.getAll());
    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<PaymentTransactionDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(paymentTransactionService.getById(id));
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<PaymentTransactionDto> update(@PathVariable Integer id, @RequestBody PaymentTransactionDto dto) {
        return ResponseEntity.ok(paymentTransactionService.update(id, dto));
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        paymentTransactionService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    // SSLCommerz Integration Endpoints
    @PostMapping("/sslcommerz/initiate")
    @Operation(summary = "Initiate SSLCommerz Payment", description = "Start a new payment transaction with SSLCommerz")
    public ResponseEntity<Map<String, Object>> initiateSSLCommerzPayment(
            @RequestParam Integer donorId,
            @RequestParam BigDecimal amount,
            @RequestParam String productName,
            @RequestParam(required = false) String productCategory) {
        
        Map<String, Object> response = paymentTransactionService.initiateSSLCommerzPayment(
            donorId, amount, productName, productCategory);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/reference/{transactionReference}")
    @Operation(summary = "Get Payment by Reference", description = "Get payment transaction by reference ID")
    public ResponseEntity<PaymentTransaction> getByTransactionReference(@PathVariable String transactionReference) {
        PaymentTransaction transaction = paymentTransactionService.getByTransactionReference(transactionReference);
        if (transaction != null) {
            return ResponseEntity.ok(transaction);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/sslcommerz/success")
    @Operation(summary = "SSLCommerz Success Callback", description = "Handle successful payment callback from SSLCommerz")
    public ResponseEntity<Map<String, String>> sslcommerzSuccess(@RequestParam Map<String, String> params) {
        String transactionRef = params.get("tran_id");
        String status = params.get("status");
        
        if (transactionRef != null && "VALID".equals(status)) {
            paymentTransactionService.updatePaymentStatus(transactionRef, status, params);
        }
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Payment processed successfully");
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/sslcommerz/fail")
    @Operation(summary = "SSLCommerz Fail Callback", description = "Handle failed payment callback from SSLCommerz")
    public ResponseEntity<Map<String, String>> sslcommerzFail(@RequestParam Map<String, String> params) {
        String transactionRef = params.get("tran_id");
        
        if (transactionRef != null) {
            paymentTransactionService.updatePaymentStatus(transactionRef, "FAILED", params);
        }
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "failed");
        response.put("message", "Payment failed");
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/sslcommerz/cancel")
    @Operation(summary = "SSLCommerz Cancel Callback", description = "Handle cancelled payment callback from SSLCommerz")
    public ResponseEntity<Map<String, String>> sslcommerzCancel(@RequestParam Map<String, String> params) {
        String transactionRef = params.get("tran_id");
        
        if (transactionRef != null) {
            paymentTransactionService.updatePaymentStatus(transactionRef, "CANCELLED", params);
        }
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "cancelled");
        response.put("message", "Payment cancelled");
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/sslcommerz/ipn")
    @Operation(summary = "SSLCommerz IPN Callback", description = "Handle Instant Payment Notification from SSLCommerz")
    public ResponseEntity<String> sslcommerzIPN(@RequestParam Map<String, String> params) {
        String transactionRef = params.get("tran_id");
        String status = params.get("status");
        
        if (transactionRef != null && status != null) {
            paymentTransactionService.updatePaymentStatus(transactionRef, status, params);
        }
        
        return ResponseEntity.ok("IPN processed");
    }
}
