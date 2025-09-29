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
    public ResponseEntity<UpazilaDto> createUpazila(@Valid @RequestBody UpazilaDto dto) {
        UpazilaDto created = upazilaService.createUpazila(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UpazilaDto> getUpazilaById(@PathVariable Integer id) {
        UpazilaDto dto = upazilaService.getUpazilaById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<UpazilaDto>> getAllUpazilas() {
        List<UpazilaDto> upazilas = upazilaService.getAllUpazilas();
        return ResponseEntity.ok(upazilas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpazilaDto> updateUpazila(
            @PathVariable Integer id,
            @Valid @RequestBody UpazilaDto dto) {
        UpazilaDto updated = upazilaService.updateUpazila(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUpazila(@PathVariable Integer id) {
        upazilaService.deleteUpazila(id);
        return ResponseEntity.noContent().build();
    }
}