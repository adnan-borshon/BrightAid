package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.PaymentTransactionDto;
import com.example.Bright_Aid.service.PaymentTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-transactions")
@RequiredArgsConstructor
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
}
