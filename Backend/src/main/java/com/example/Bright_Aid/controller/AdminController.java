package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.AdminDto;
import com.example.Bright_Aid.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminService adminService;

    @PostMapping
    public ResponseEntity<AdminDto> saveAdmin(@Valid @RequestBody AdminDto adminDto) {
        AdminDto savedAdmin = adminService.saveAdmin(adminDto);
        return new ResponseEntity<>(savedAdmin, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AdminDto>> getAllAdmins() {
        List<AdminDto> admins = adminService.getAllAdmins();
        return ResponseEntity.ok(admins);
    }

    @GetMapping("/{adminId}")
    public ResponseEntity<AdminDto> getAdminById(@PathVariable Integer adminId) {
        AdminDto admin = adminService.getAdminById(adminId);
        return ResponseEntity.ok(admin);
    }

    @DeleteMapping("/{adminId}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Integer adminId) {
        adminService.deleteAdmin(adminId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{adminId}/verified-ngos")
    public ResponseEntity<List<Integer>> getVerifiedNgoIds(@PathVariable Integer adminId) {
        List<Integer> ngoIds = adminService.getVerifiedNgoIds(adminId);
        return ResponseEntity.ok(ngoIds);
    }
}