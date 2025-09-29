package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.VerificationDocumentDto;
import com.example.Bright_Aid.service.VerificationDocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/verification-documents")
@RequiredArgsConstructor
public class VerificationDocumentController {

    private final VerificationDocumentService verificationDocumentService;

    @PostMapping
    public ResponseEntity<VerificationDocumentDto> createVerificationDocument(@Valid @RequestBody VerificationDocumentDto dto) {
        VerificationDocumentDto created = verificationDocumentService.createVerificationDocument(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VerificationDocumentDto> getVerificationDocumentById(@PathVariable Integer id) {
        VerificationDocumentDto dto = verificationDocumentService.getVerificationDocumentById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<VerificationDocumentDto>> getAllVerificationDocuments() {
        List<VerificationDocumentDto> documents = verificationDocumentService.getAllVerificationDocuments();
        return ResponseEntity.ok(documents);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VerificationDocumentDto> updateVerificationDocument(
            @PathVariable Integer id,
            @Valid @RequestBody VerificationDocumentDto dto) {
        VerificationDocumentDto updated = verificationDocumentService.updateVerificationDocument(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVerificationDocument(@PathVariable Integer id) {
        verificationDocumentService.deleteVerificationDocument(id);
        return ResponseEntity.noContent().build();
    }
}