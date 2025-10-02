package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Dto.DonorGamificationDto;
import com.example.Bright_Aid.Entity.Donor;
import com.example.Bright_Aid.Entity.DonorGamification;
import com.example.Bright_Aid.repository.DonorGamificationRepository;
import com.example.Bright_Aid.repository.DonorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DonorGamificationService {

    private final DonorGamificationRepository gamificationRepository;
    private final DonorRepository donorRepository;

    public List<DonorGamificationDto> getAllGamifications() {
        return gamificationRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public DonorGamificationDto getGamificationById(Integer id) {
        return gamificationRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Gamification not found"));
    }

    @Transactional
    public DonorGamificationDto create(DonorGamificationDto dto) {
        Donor donor = donorRepository.findById(dto.getDonorIdRequest())
                .orElseThrow(() -> new RuntimeException("Donor not found with ID: " + dto.getDonorIdRequest()));

        DonorGamification gamification = DonorGamification.builder()
                .donor(donor)
                .totalPoints(dto.getTotalPointsRequest())
                .currentLevel(dto.getCurrentLevelRequest())
                .badgesEarned(dto.getBadgesEarnedRequest())
                .lastUpdated(LocalDateTime.now())
                .build();

        return toDto(gamificationRepository.save(gamification));
    }

    // Get all donors for reference
    public List<Donor> getAllDonors() {
        return donorRepository.findAll();
    }

    @Transactional
    public DonorGamificationDto update(Integer id, DonorGamificationDto dto) {
        DonorGamification gamification = gamificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gamification not found"));

        if (dto.getTotalPointsRequest() != null) gamification.setTotalPoints(dto.getTotalPointsRequest());
        if (dto.getCurrentLevelRequest() != null) gamification.setCurrentLevel(dto.getCurrentLevelRequest());
        if (dto.getBadgesEarnedRequest() != null) gamification.setBadgesEarned(dto.getBadgesEarnedRequest());

        gamification.setLastUpdated(LocalDateTime.now());

        return toDto(gamificationRepository.save(gamification));
    }

    private DonorGamificationDto toDto(DonorGamification entity) {
        // Calculate progress to next level
        int points = entity.getTotalPoints() != null ? entity.getTotalPoints() : 0;
        String level = entity.getCurrentLevel() != null ? entity.getCurrentLevel() : "Bronze";
        
        int pointsToNext = calculatePointsToNextLevel(points, level);
        double progressPercent = calculateProgressPercentage(points, level);
        String nextLevel = getNextLevel(level);
        
        // Calculate ranking dynamically
        Integer ranking = gamificationRepository.calculateDonorRanking(entity.getDonor().getDonorId());
        
        return DonorGamificationDto.builder()
                .gamificationId(entity.getGamificationId())
                .donorId(entity.getDonor().getDonorId())
                .donorName(entity.getDonorName())
                .organizationName(entity.getDonor().getDonorName())
                .totalPoints(entity.getTotalPoints())
                .currentLevel(entity.getCurrentLevel())
                .badgesEarned(entity.getBadgesEarned())
                .rankingPosition(ranking != null ? ranking : 999)
                .lastUpdated(entity.getLastUpdated())
                .pointsToNextLevel(pointsToNext)
                .progressPercentage(progressPercent)
                .nextLevel(nextLevel)
                .build();
    }

    @Transactional
    public void deleteGamification(Integer id) {
        gamificationRepository.deleteById(id);
    }

    // Get gamification by donor ID
    public DonorGamificationDto getGamificationByDonorId(Integer donorId) {
        return gamificationRepository.findByDonor_DonorId(donorId)
                .map(this::toDto)
                .orElse(createDefaultGamification(donorId));
    }

    // Create default gamification for new donors
    private DonorGamificationDto createDefaultGamification(Integer donorId) {
        Donor donor = donorRepository.findById(donorId)
                .orElseThrow(() -> new RuntimeException("Donor not found"));
        
        return DonorGamificationDto.builder()
                .donorId(donorId)
                .donorName(donor.getDonorName())
                .organizationName(donor.getDonorName())
                .totalPoints(0)
                .currentLevel("Bronze")
                .badgesEarned(java.util.Arrays.asList("New Donor"))
                .rankingPosition(999) // Default ranking for new donors
                .lastUpdated(java.time.LocalDateTime.now())
                .pointsToNextLevel(1000)
                .progressPercentage(0.0)
                .nextLevel("Silver")
                .build();
    }
    
    // Helper methods for progress calculation
    private int calculatePointsToNextLevel(int currentPoints, String level) {
        return switch (level) {
            case "Bronze" -> Math.max(0, 1000 - currentPoints);
            case "Silver" -> Math.max(0, 5000 - currentPoints);
            case "Gold" -> Math.max(0, 15000 - currentPoints);
            case "Platinum" -> 0;
            default -> 1000 - currentPoints;
        };
    }
    
    private double calculateProgressPercentage(int currentPoints, String level) {
        return switch (level) {
            case "Bronze" -> {
                // Bronze: 0-999 points, progress within this range
                yield Math.min(100.0, Math.max(0.0, (currentPoints / 1000.0) * 100));
            }
            case "Silver" -> {
                // Silver: 1000-4999 points, progress within this range
                if (currentPoints < 1000) yield 0.0;
                if (currentPoints >= 5000) yield 100.0;
                yield ((currentPoints - 1000) / 4000.0) * 100;
            }
            case "Gold" -> {
                // Gold: 5000-14999 points, progress within this range
                if (currentPoints < 5000) yield 0.0;
                if (currentPoints >= 15000) yield 100.0;
                yield ((currentPoints - 5000) / 10000.0) * 100;
            }
            case "Platinum" -> 100.0; // Max level
            default -> Math.min(100.0, Math.max(0.0, (currentPoints / 1000.0) * 100));
        };
    }
    
    private String getNextLevel(String currentLevel) {
        return switch (currentLevel) {
            case "Bronze" -> "Silver";
            case "Silver" -> "Gold";
            case "Gold" -> "Platinum";
            case "Platinum" -> "Max Level";
            default -> "Silver";
        };
    }
    
    // Get unique schools count for donor based on sponsored students and projects
    public Integer getUniqueSchoolsCount(Integer donorId) {
        return gamificationRepository.getUniqueSchoolsCountByDonor(donorId);
    }
    
    // Get donor statistics (calculated dynamically)
    public com.example.Bright_Aid.Dto.DonorStatsDto getDonorStats(Integer donorId) {
        Donor donor = donorRepository.findById(donorId)
                .orElseThrow(() -> new RuntimeException("Donor not found"));
        
        return com.example.Bright_Aid.Dto.DonorStatsDto.builder()
                .donorId(donor.getDonorId())
                .donorName(donor.getDonorName())
                .taxId(donor.getTaxId())
                .isAnonymous(donor.getIsAnonymous())
                .totalDonated(donorRepository.calculateTotalDonated(donorId))
                .totalSchoolsSupported(donorRepository.calculateTotalSchoolsSupported(donorId))
                .totalStudentsSponsored(donorRepository.calculateTotalStudentsSponsored(donorId))
                .totalProjectsDonated(donorRepository.calculateTotalProjectsDonated(donorId))
                .build();
    }
}