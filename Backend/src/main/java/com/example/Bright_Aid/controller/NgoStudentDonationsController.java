package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.NgoStudentDonationsDTO;
import com.example.Bright_Aid.service.NgoStudentDonationsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ngo-student-donations")
public class NgoStudentDonationsController {

    private final NgoStudentDonationsService service;

    public NgoStudentDonationsController(NgoStudentDonationsService service) {
        this.service = service;
    }

    // ===================== CREATE =====================
    @PostMapping
    public ResponseEntity<NgoStudentDonationsDTO> create(@RequestBody NgoStudentDonationsDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    // ===================== UPDATE =====================
    @PutMapping("/{id}")
    public ResponseEntity<NgoStudentDonationsDTO> update(@PathVariable Integer id,
                                                         @RequestBody NgoStudentDonationsDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    // ===================== GET BY ID =====================
    @GetMapping("/{id}")
    public ResponseEntity<NgoStudentDonationsDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // ===================== GET ALL =====================
    @GetMapping
    public ResponseEntity<List<NgoStudentDonationsDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // ===================== DELETE =====================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
