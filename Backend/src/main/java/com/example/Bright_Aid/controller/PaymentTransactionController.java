package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.PaymentTransactionDto;
import com.example.Bright_Aid.Entity.PaymentTransaction;
import com.example.Bright_Aid.service.PaymentTransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-transactions")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class PaymentTransactionController {

    private final PaymentTransactionService paymentTransactionService;

    @PostMapping
    public ResponseEntity<PaymentTransactionDto> createPaymentTransaction(@Valid @RequestBody PaymentTransactionDto paymentTransactionDto) {
        log.info("Creating new payment transaction for donor ID: {}", paymentTransactionDto.getDonorId());

        PaymentTransactionDto createdTransaction = paymentTransactionService.createPaymentTransaction(paymentTransactionDto);

        log.info("Successfully created payment transaction with ID: {}", createdTransaction.getTransactionId());
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentTransactionDto> getPaymentTransactionById(@PathVariable Integer id) {
        log.info("Fetching payment transaction with ID: {}", id);

        PaymentTransactionDto transaction = paymentTransactionService.getPaymentTransactionById(id);

        log.info("Successfully retrieved payment transaction with ID: {}", id);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping
    public ResponseEntity<List<PaymentTransactionDto>> getAllPaymentTransactions() {
        log.info("Fetching all payment transactions");

        List<PaymentTransactionDto> transactions = paymentTransactionService.getAllPaymentTransactions();

        log.info("Successfully retrieved {} payment transactions", transactions.size());
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<PaymentTransactionDto>> getAllPaymentTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "transactionId") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        log.info("Fetching paginated payment transactions - Page: {}, Size: {}, Sort: {} {}", page, size, sortBy, sortDir);

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<PaymentTransactionDto> transactions = paymentTransactionService.getAllPaymentTransactions(pageable);

        log.info("Successfully retrieved {} payment transactions out of {} total",
                transactions.getNumberOfElements(), transactions.getTotalElements());
        return ResponseEntity.ok(transactions);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentTransactionDto> updatePaymentTransaction(@PathVariable Integer id,
                                                                          @Valid @RequestBody PaymentTransactionDto paymentTransactionDto) {
        log.info("Updating payment transaction with ID: {}", id);

        PaymentTransactionDto updatedTransaction = paymentTransactionService.updatePaymentTransaction(id, paymentTransactionDto);

        log.info("Successfully updated payment transaction with ID: {}", id);
        return ResponseEntity.ok(updatedTransaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentTransaction(@PathVariable Integer id) {
        log.info("Deleting payment transaction with ID: {}", id);

        paymentTransactionService.deletePaymentTransaction(id);

        log.info("Successfully deleted payment transaction with ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PaymentTransactionDto>> getPaymentTransactionsByStatus(@PathVariable PaymentTransaction.TransactionStatus status) {
        log.info("Fetching payment transactions with status: {}", status);

        List<PaymentTransactionDto> transactions = paymentTransactionService.getPaymentTransactionsByStatus(status);

        log.info("Successfully retrieved {} payment transactions with status: {}", transactions.size(), status);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/donor/{donorId}")
    public ResponseEntity<List<PaymentTransactionDto>> getPaymentTransactionsByDonor(@PathVariable Integer donorId) {
        log.info("Fetching payment transactions for donor ID: {}", donorId);

        List<PaymentTransactionDto> transactions = paymentTransactionService.getPaymentTransactionsByDonor(donorId);

        log.info("Successfully retrieved {} payment transactions for donor ID: {}", transactions.size(), donorId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<PaymentTransactionDto>> getPaymentTransactionsByType(@PathVariable PaymentTransaction.TransactionType type) {
        log.info("Fetching payment transactions with type: {}", type);

        List<PaymentTransactionDto> transactions = paymentTransactionService.getPaymentTransactionsByType(type);

        log.info("Successfully retrieved {} payment transactions with type: {}", transactions.size(), type);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/payment-method/{method}")
    public ResponseEntity<List<PaymentTransactionDto>> getPaymentTransactionsByPaymentMethod(@PathVariable PaymentTransaction.PaymentMethod method) {
        log.info("Fetching payment transactions with payment method: {}", method);

        List<PaymentTransactionDto> transactions = paymentTransactionService.getPaymentTransactionsByPaymentMethod(method);

        log.info("Successfully retrieved {} payment transactions with payment method: {}", transactions.size(), method);
        return ResponseEntity.ok(transactions);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PaymentTransactionDto> updateTransactionStatus(@PathVariable Integer id,
                                                                         @RequestParam PaymentTransaction.TransactionStatus status) {
        log.info("Updating transaction status for ID: {} to status: {}", id, status);

        PaymentTransactionDto updatedTransaction = paymentTransactionService.updateTransactionStatus(id, status);

        log.info("Successfully updated transaction status for ID: {} to status: {}", id, status);
        return ResponseEntity.ok(updatedTransaction);
    }

    @PatchMapping("/{id}/initiate")
    public ResponseEntity<PaymentTransactionDto> initiateTransaction(@PathVariable Integer id) {
        log.info("Initiating payment transaction with ID: {}", id);

        PaymentTransactionDto initiatedTransaction = paymentTransactionService.initiateTransaction(id);

        log.info("Successfully initiated payment transaction with ID: {}", id);
        return ResponseEntity.ok(initiatedTransaction);
    }
}