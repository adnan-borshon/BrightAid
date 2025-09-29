package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Entity.Division;
import com.example.Bright_Aid.Entity.Donor;
import com.example.Bright_Aid.Entity.DonorGamification;
import com.example.Bright_Aid.Entity.User;
import com.example.Bright_Aid.Dto.DonorDto;
import com.example.Bright_Aid.repository.DivisionRepository;
import com.example.Bright_Aid.repository.DonorRepository;
import com.example.Bright_Aid.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DonorService {

    private final DonorRepository donorRepository;
    private final UserRepository userRepository;
    private final DivisionRepository divisionRepository;

    public DonorService(DonorRepository donorRepository,
                        UserRepository userRepository,
                        DivisionRepository divisionRepository) {
        this.donorRepository = donorRepository;
        this.userRepository = userRepository;
        this.divisionRepository = divisionRepository;
    }

    // Create or update Donor
    public DonorDto saveDonor(DonorDto donorDto) {
        User user = userRepository.findById(donorDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if donor already exists for this user
        Donor existingDonor = donorRepository.findById(donorDto.getUserId()).orElse(null);
        if (existingDonor != null && donorDto.getDonorId() == null) {
            throw new RuntimeException("Donor already exists for user ID: " + donorDto.getUserId());
        }

        Division division = null;
        if (donorDto.getDivisionId() != null) {
            division = divisionRepository.findById(donorDto.getDivisionId())
                    .orElseThrow(() -> new RuntimeException("Division not found"));
        }

        Donor donor = Donor.builder()
                .donorId(donorDto.getDonorId())
                .user(user)
                .organizationName(donorDto.getOrganizationName())
                .taxId(donorDto.getTaxId())
                .isAnonymous(donorDto.getIsAnonymous() != null ?
                        donorDto.getIsAnonymous() : false)
                .totalDonated(donorDto.getTotalDonated() != null ?
                        donorDto.getTotalDonated() : BigDecimal.ZERO)
                .totalSchoolsSupported(donorDto.getTotalSchoolsSupported() != null ?
                        donorDto.getTotalSchoolsSupported() : 0)
                .totalStudentsSponsored(donorDto.getTotalStudentsSponsored() != null ?
                        donorDto.getTotalStudentsSponsored() : 0)
                .division(division)
                .build();

        Donor saved = donorRepository.save(donor);
        return mapToDto(saved);
    }

    // Get all donors
    public List<DonorDto> getAllDonors() {
        return donorRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Get donor by ID
    public DonorDto getDonorById(Integer donorId) {
        return donorRepository.findById(donorId)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Donor not found"));
    }

    // Delete donor
    public void deleteDonor(Integer donorId) {
        if (!donorRepository.existsById(donorId)) {
            throw new RuntimeException("Donor not found");
        }
        donorRepository.deleteById(donorId);
    }

    // Get all gamification records for a donor
    public List<Integer> getGamificationIds(Integer donorId) {
        Donor donor = donorRepository.findById(donorId)
                .orElseThrow(() -> new RuntimeException("Donor not found"));
        List<DonorGamification> gamifications = donor.getGamifications();
        if (gamifications == null) return List.of();
        return gamifications.stream().map(DonorGamification::getGamificationId).collect(Collectors.toList());
    }

    // Update donation statistics
    public DonorDto updateDonationStats(Integer donorId, BigDecimal additionalAmount,
                                        Integer additionalSchools, Integer additionalStudents) {
        Donor donor = donorRepository.findById(donorId)
                .orElseThrow(() -> new RuntimeException("Donor not found"));

        if (additionalAmount != null) {
            donor.setTotalDonated(donor.getTotalDonated().add(additionalAmount));
        }

        if (additionalSchools != null) {
            donor.setTotalSchoolsSupported(donor.getTotalSchoolsSupported() + additionalSchools);
        }

        if (additionalStudents != null) {
            donor.setTotalStudentsSponsored(donor.getTotalStudentsSponsored() + additionalStudents);
        }

        Donor saved = donorRepository.save(donor);
        return mapToDto(saved);
    }

    // Toggle anonymous status
    public DonorDto toggleAnonymousStatus(Integer donorId) {
        Donor donor = donorRepository.findById(donorId)
                .orElseThrow(() -> new RuntimeException("Donor not found"));

        donor.setIsAnonymous(!donor.getIsAnonymous());

        Donor saved = donorRepository.save(donor);
        return mapToDto(saved);
    }

    // Map Donor entity to DTO
    private DonorDto mapToDto(Donor donor) {
        User user = donor.getUser();

        // Get user full name from UserProfile if available
        String userFullName = "N/A";
        if (user.getUserProfile() != null) {
            String FullName = user.getUserProfile().getFullName() != null ? user.getUserProfile().getFullName() : "";
            userFullName = (FullName ).trim();
            if (userFullName.isEmpty()) {
                userFullName = user.getUsername();
            }
        } else {
            userFullName = user.getUsername();
        }

        List<Integer> gamificationIds = donor.getGamifications() != null ?
                donor.getGamifications().stream().map(DonorGamification::getGamificationId).toList() :
                List.of();

        return DonorDto.builder()
                .donorId(donor.getDonorId())
                .userId(user.getUserId())
                .username(user.getUsername())
                .userEmail(user.getEmail())
                .userFullName(userFullName)
                .organizationName(donor.getOrganizationName())
                .taxId(donor.getTaxId())
                .isAnonymous(donor.getIsAnonymous())
                .totalDonated(donor.getTotalDonated())
                .totalSchoolsSupported(donor.getTotalSchoolsSupported())
                .totalStudentsSponsored(donor.getTotalStudentsSponsored())
                .divisionId(donor.getDivision() != null ? donor.getDivision().getDivisionId() : null)
                .divisionName(donor.getDivision() != null ? donor.getDivision().getDivisionName() : null)
                .gamificationIds(gamificationIds)
                .createdAt(donor.getCreatedAt())
                .updatedAt(donor.getUpdatedAt())
                .build();
    }
}
