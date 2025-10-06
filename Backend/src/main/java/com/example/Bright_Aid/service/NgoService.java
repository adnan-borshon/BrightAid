package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Dto.NgoDto;
import com.example.Bright_Aid.Entity.Ngo;
import com.example.Bright_Aid.Entity.User;
// import com.example.Bright_Aid.Entity.Admin;
import com.example.Bright_Aid.Entity.NgoProject;
import com.example.Bright_Aid.repository.NgoRepository;
import com.example.Bright_Aid.repository.UserRepository;
// import com.example.Bright_Aid.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class NgoService {

    private final NgoRepository ngoRepository;
    private final UserRepository userRepository;
    // private final AdminRepository adminRepository;

    @Transactional
    public NgoDto createNgo(NgoDto ngoDto) {
        log.info("Creating NGO with name: {}", ngoDto.getNgoName());

        User user = userRepository.findById(ngoDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + ngoDto.getUserId()));

        // Check if NGO already exists for this user
        boolean ngoExists = ngoRepository.findAll().stream()
                .anyMatch(ngo -> ngo.getUser().getUserId().equals(ngoDto.getUserId()));
        if (ngoExists) {
            throw new RuntimeException("NGO already exists for user ID: " + ngoDto.getUserId());
        }

        Ngo.NgoBuilder ngoBuilder = Ngo.builder()
                .user(user)
                .ngoName(ngoDto.getNgoName())
                .registrationNumber(ngoDto.getRegistrationNumber())
                .description(ngoDto.getDescription())
                .contactPerson(ngoDto.getContactPerson())
                .contactPhone(ngoDto.getContactPhone())
                .verificationStatus(ngoDto.getVerificationStatus());



        // Set verified at timestamp if provided
        if (ngoDto.getVerifiedAt() != null) {
            ngoBuilder.verifiedAt(ngoDto.getVerifiedAt());
        }

        Ngo ngo = ngoBuilder.build();
        Ngo savedNgo = ngoRepository.save(ngo);

        log.info("Successfully created NGO with ID: {}", savedNgo.getNgoId());
        return convertToDto(savedNgo);
    }

    public NgoDto getNgoById(Integer ngoId) {
        log.info("Fetching NGO with ID: {}", ngoId);

        Ngo ngo = ngoRepository.findById(ngoId)
                .orElseThrow(() -> new RuntimeException("NGO not found with ID: " + ngoId));

        return convertToDto(ngo);
    }

    public List<NgoDto> getAllNgos() {
        log.info("Fetching all NGOs");

        List<Ngo> ngos = ngoRepository.findAll();
        return ngos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<NgoDto> getAllNgos(Pageable pageable) {
        log.info("Fetching NGOs with pagination: {}", pageable);

        Page<Ngo> ngos = ngoRepository.findAll(pageable);
        return ngos.map(this::convertToDto);
    }

    @Transactional
    public NgoDto updateNgo(Integer ngoId, NgoDto ngoDto) {
        log.info("Updating NGO with ID: {}", ngoId);

        Ngo existingNgo = ngoRepository.findById(ngoId)
                .orElseThrow(() -> new RuntimeException("NGO not found with ID: " + ngoId));

        // Update User if provided
        if (ngoDto.getUserId() != null &&
                !ngoDto.getUserId().equals(existingNgo.getUser().getUserId())) {
            User user = userRepository.findById(ngoDto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + ngoDto.getUserId()));
            existingNgo.setUser(user);
        }



        if (ngoDto.getNgoName() != null) {
            existingNgo.setNgoName(ngoDto.getNgoName());
        }
        if (ngoDto.getRegistrationNumber() != null) {
            existingNgo.setRegistrationNumber(ngoDto.getRegistrationNumber());
        }
        if (ngoDto.getDescription() != null) {
            existingNgo.setDescription(ngoDto.getDescription());
        }
        if (ngoDto.getContactPerson() != null) {
            existingNgo.setContactPerson(ngoDto.getContactPerson());
        }
        if (ngoDto.getContactPhone() != null) {
            existingNgo.setContactPhone(ngoDto.getContactPhone());
        }
        if (ngoDto.getVerificationStatus() != null) {
            existingNgo.setVerificationStatus(ngoDto.getVerificationStatus());
        }
        if (ngoDto.getVerifiedAt() != null) {
            existingNgo.setVerifiedAt(ngoDto.getVerifiedAt());
        }

        Ngo updatedNgo = ngoRepository.save(existingNgo);
        log.info("Successfully updated NGO with ID: {}", ngoId);

        return convertToDto(updatedNgo);
    }

    @Transactional
    public void deleteNgo(Integer ngoId) {
        log.info("Deleting NGO with ID: {}", ngoId);

        if (!ngoRepository.existsById(ngoId)) {
            throw new RuntimeException("NGO not found with ID: " + ngoId);
        }

        ngoRepository.deleteById(ngoId);
        log.info("Successfully deleted NGO with ID: {}", ngoId);
    }

    public List<NgoDto> getNgosByVerificationStatus(Ngo.VerificationStatus status) {
        log.info("Fetching NGOs with verification status: {}", status);

        List<Ngo> ngos = ngoRepository.findAll();
        return ngos.stream()
                .filter(ngo -> ngo.getVerificationStatus().equals(status))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<NgoDto> getVerifiedNgos() {
        log.info("Fetching all verified NGOs");

        List<Ngo> ngos = ngoRepository.findAll();
        return ngos.stream()
                .filter(ngo -> ngo.getVerificationStatus().equals(Ngo.VerificationStatus.VERIFIED))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<NgoDto> getPendingNgos() {
        log.info("Fetching all pending NGOs");

        List<Ngo> ngos = ngoRepository.findAll();
        return ngos.stream()
                .filter(ngo -> ngo.getVerificationStatus().equals(Ngo.VerificationStatus.PENDING))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public NgoDto verifyNgo(Integer ngoId, Integer adminId) {
        log.info("Verifying NGO with ID: {} by admin ID: {}", ngoId, adminId);

        Ngo ngo = ngoRepository.findById(ngoId)
                .orElseThrow(() -> new RuntimeException("NGO not found with ID: " + ngoId));

        // Admin admin = adminRepository.findById(adminId)
        //         .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + adminId));

        ngo.setVerificationStatus(Ngo.VerificationStatus.VERIFIED);
        ngo.setVerifiedAt(LocalDateTime.now());

        Ngo updatedNgo = ngoRepository.save(ngo);
        log.info("Successfully verified NGO with ID: {}", ngoId);

        return convertToDto(updatedNgo);
    }

    @Transactional
    public NgoDto rejectNgo(Integer ngoId, Integer adminId) {
        log.info("Rejecting NGO with ID: {} by admin ID: {}", ngoId, adminId);

        Ngo ngo = ngoRepository.findById(ngoId)
                .orElseThrow(() -> new RuntimeException("NGO not found with ID: " + ngoId));

        // Admin admin = adminRepository.findById(adminId)
        //         .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + adminId));

        ngo.setVerificationStatus(Ngo.VerificationStatus.REJECTED);
        ngo.setVerifiedAt(LocalDateTime.now());

        Ngo updatedNgo = ngoRepository.save(ngo);
        log.info("Successfully rejected NGO with ID: {}", ngoId);

        return convertToDto(updatedNgo);
    }

    public NgoDto getNgoByRegistrationNumber(String registrationNumber) {
        log.info("Fetching NGO with registration number: {}", registrationNumber);

        List<Ngo> ngos = ngoRepository.findAll();
        Ngo ngo = ngos.stream()
                .filter(n -> n.getRegistrationNumber().equals(registrationNumber))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("NGO not found with registration number: " + registrationNumber));

        return convertToDto(ngo);
    }

    public NgoDto getNgoByUserId(Integer userId) {
        log.info("Fetching NGO for user ID: {}", userId);

        List<Ngo> ngos = ngoRepository.findAll();
        Ngo ngo = ngos.stream()
                .filter(n -> n.getUser().getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("NGO not found for user ID: " + userId));

        return convertToDto(ngo);
    }

    private NgoDto convertToDto(Ngo ngo) {
        return NgoDto.builder()
                .ngoId(ngo.getNgoId())
                .userId(ngo.getUser().getUserId())
                .ngoName(ngo.getNgoName())
                .registrationNumber(ngo.getRegistrationNumber())
                .description(ngo.getDescription())
                .contactPerson(ngo.getContactPerson())
                .contactPhone(ngo.getContactPhone())
                .verificationStatus(ngo.getVerificationStatus())
                .verifiedAt(ngo.getVerifiedAt())
                .createdAt(ngo.getCreatedAt())
                .updatedAt(ngo.getUpdatedAt())
                .build();
    }
}