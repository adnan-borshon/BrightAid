package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.UpazilaDto;
import com.example.Bright_Aid.service.UpazilaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/upazilas")
@RequiredArgsConstructor
public class UpazilaController {

    private final UpazilaService upazilaService;

    @PostMapping
    public ResponseEntity<?> createUpazila(@Valid @RequestBody UpazilaDto dto) {
        try {
            UpazilaDto created = upazilaService.createUpazila(dto);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUpazilaById(@PathVariable Integer id) {
        try {
            UpazilaDto dto = upazilaService.getUpazilaById(id);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<UpazilaDto>> getAllUpazilas() {
        List<UpazilaDto> upazilas = upazilaService.getAllUpazilas();
        return ResponseEntity.ok(upazilas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUpazila(
            @PathVariable Integer id,
            @Valid @RequestBody UpazilaDto dto) {
        try {
            UpazilaDto updated = upazilaService.updateUpazila(id, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUpazila(@PathVariable Integer id) {
        try {
            upazilaService.deleteUpazila(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}