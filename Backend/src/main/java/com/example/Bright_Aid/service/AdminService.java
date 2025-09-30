package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Entity.Admin;
import com.example.Bright_Aid.Entity.Ngo;
import com.example.Bright_Aid.Entity.User;
import com.example.Bright_Aid.Dto.AdminDto;
import com.example.Bright_Aid.repository.AdminRepository;
import com.example.Bright_Aid.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    public AdminService(AdminRepository adminRepository, UserRepository userRepository) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
    }

    // Create or update Admin safely
    public AdminDto saveAdmin(AdminDto adminDto) {
        User user = userRepository.findById(adminDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Admin admin;
        if (adminDto.getAdminId() != null) {
            // Update existing admin
            admin = adminRepository.findById(adminDto.getAdminId())
                    .orElseThrow(() -> new RuntimeException("Admin not found"));
            admin.setUser(user);
            admin.setPermissions(adminDto.getPermissions());
            admin.setIsActive(adminDto.getIsActive() != null ? adminDto.getIsActive() : true);
            admin.setAssignedAt(adminDto.getAssignedAt());
            admin.setAdminNotes(adminDto.getAdminNotes());
        } else {
            // Create new admin
            admin = Admin.builder()
                    .user(user)
                    .permissions(adminDto.getPermissions())
                    .isActive(adminDto.getIsActive() != null ? adminDto.getIsActive() : true)
                    .assignedAt(adminDto.getAssignedAt())
                    .adminNotes(adminDto.getAdminNotes())
                    .build();
        }

        Admin saved = adminRepository.save(admin);
        return mapToDto(saved);
    }

    public List<AdminDto> getAllAdmins() {
        return adminRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public AdminDto getAdminById(Integer adminId) {
        return adminRepository.findById(adminId)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
    }

    public void deleteAdmin(Integer adminId) {
        if (!adminRepository.existsById(adminId)) {
            throw new RuntimeException("Admin not found");
        }
        adminRepository.deleteById(adminId);
    }

    public List<Integer> getVerifiedNgoIds(Integer adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        List<Ngo> verifiedNgos = admin.getVerifiedNgos();
        if (verifiedNgos == null) return List.of();
        return verifiedNgos.stream().map(Ngo::getNgoId).collect(Collectors.toList());
    }

    private AdminDto mapToDto(Admin admin) {
        List<Integer> ngoIds = admin.getVerifiedNgos() != null ?
                admin.getVerifiedNgos().stream().map(Ngo::getNgoId).toList() :
                List.of();

        return AdminDto.builder()
                .adminId(admin.getAdminId())
                .userId(admin.getUser().getUserId())
                .permissions(admin.getPermissions())
                .isActive(admin.getIsActive())
                .assignedAt(admin.getAssignedAt())
                .adminNotes(admin.getAdminNotes())
                .verifiedNgoIds(ngoIds)
                .build();
    }
}
