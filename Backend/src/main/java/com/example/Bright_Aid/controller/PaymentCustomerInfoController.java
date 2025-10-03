package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.PaymentCustomerInfoDto;
import com.example.Bright_Aid.Entity.PaymentCustomerInfo;
import com.example.Bright_Aid.service.PaymentCustomerInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-customer-info")
@RequiredArgsConstructor
public class PaymentCustomerInfoController {

    private final PaymentCustomerInfoService service;

    @GetMapping
    public List<PaymentCustomerInfoDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public PaymentCustomerInfoDto getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @PostMapping
    public PaymentCustomerInfoDto create(@RequestBody PaymentCustomerInfoDto dto) {
        return service.save(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
