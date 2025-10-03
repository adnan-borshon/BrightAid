package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.NgoProjectDonationsDTO;
import com.example.Bright_Aid.service.NgoProjectDonationsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ngo-project-donations")
public class NgoProjectDonationsController {

    private final NgoProjectDonationsService service;

    public NgoProjectDonationsController(NgoProjectDonationsService service) {
        this.service = service;
    }

    // ===================== CREATE =====================
    @PostMapping
    public ResponseEntity<NgoProjectDonationsDTO> create(@RequestBody NgoProjectDonationsDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    // ===================== UPDATE =====================
    @PutMapping("/{id}")
    public ResponseEntity<NgoProjectDonationsDTO> update(@PathVariable Integer id,
                                                         @RequestBody NgoProjectDonationsDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    // ===================== GET BY ID =====================
    @GetMapping("/{id}")
    public ResponseEntity<NgoProjectDonationsDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // ===================== GET ALL =====================
    @GetMapping
    public ResponseEntity<List<NgoProjectDonationsDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // ===================== DELETE =====================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
