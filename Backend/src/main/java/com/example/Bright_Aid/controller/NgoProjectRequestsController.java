package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.NgoProjectRequestsDTO;
import com.example.Bright_Aid.service.NgoProjectRequestsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ngo-project-requests")
public class NgoProjectRequestsController {

    private final NgoProjectRequestsService service;

    public NgoProjectRequestsController(NgoProjectRequestsService service) {
        this.service = service;
    }

    // ===================== CREATE =====================
    @PostMapping
    public ResponseEntity<NgoProjectRequestsDTO> create(@RequestBody NgoProjectRequestsDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    // ===================== UPDATE =====================
    @PutMapping("/{id}")
    public ResponseEntity<NgoProjectRequestsDTO> update(@PathVariable Integer id,
                                                        @RequestBody NgoProjectRequestsDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    // ===================== GET BY ID =====================
    @GetMapping("/{id}")
    public ResponseEntity<NgoProjectRequestsDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // ===================== GET ALL =====================
    @GetMapping
    public ResponseEntity<List<NgoProjectRequestsDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // ===================== DELETE =====================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
