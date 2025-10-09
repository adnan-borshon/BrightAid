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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DonorGamificationService {

    private final DonorGamificationRepository donorGamificationRepository;
    private final DonorRepository donorRepository;

    public List<DonorGamificationDto> getAllDonorGamification() {
        return donorGamificationRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public DonorGamificationDto getDonorGamificationByDonorId(Integer donorId) {
        DonorGamification gamification = donorGamificationRepository.findByDonorDonorId(donorId)
                .orElseThrow(() -> new RuntimeException("Donor gamification not found"));
        return convertToDto(gamification);
    }

    public DonorGamificationDto createOrUpdateDonorGamification(DonorGamificationDto dto) {
        Donor donor = donorRepository.findById(dto.getDonorId())
                .orElseThrow(() -> new RuntimeException("Donor not found"));

        DonorGamification gamification = donorGamificationRepository.findByDonorDonorId(dto.getDonorId())
                .orElse(DonorGamification.builder()
                        .donor(donor)
                        .totalPoints(0)
                        .impactScore(0.0)
                        .badgesEarned(new ArrayList<>())
                        .lastUpdated(LocalDateTime.now())
                        .build());

        // Update fields
        if (dto.getTotalPoints() != null) {
            gamification.setTotalPoints(dto.getTotalPoints());
        }
        if (dto.getImpactScore() != null) {
            gamification.setImpactScore(dto.getImpactScore());
        }
        if (dto.getBadgesEarned() != null) {
            gamification.setBadgesEarned(dto.getBadgesEarned());
        }
        if (dto.getDonorName() != null) {
            gamification.setDonorName(dto.getDonorName());
        }
        
        gamification.setLastUpdated(LocalDateTime.now());

        DonorGamification saved = donorGamificationRepository.save(gamification);
        return convertToDto(saved);
    }

    public void deleteDonorGamification(Integer donorId) {
        DonorGamification gamification = donorGamificationRepository.findByDonorDonorId(donorId)
                .orElseThrow(() -> new RuntimeException("Donor gamification not found"));
        donorGamificationRepository.delete(gamification);
    }

    public Integer getUniqueSchoolsCountByDonor(Integer donorId) {
        // This would require a native query to count unique schools from donations
        // For now, return a default value
        return donorGamificationRepository.getUniqueSchoolsCountByDonor(donorId);
    }

    public Map<String, Object> getDonorStats(Integer donorId) {
        DonorGamification gamification = donorGamificationRepository.findByDonorDonorId(donorId)
                .orElse(null);
        
        Map<String, Object> stats = new java.util.HashMap<>();
        if (gamification != null) {
            stats.put("totalPoints", gamification.getTotalPoints());
            stats.put("impactScore", gamification.getImpactScore());
            stats.put("badgesEarned", gamification.getBadgesEarned() != null ? gamification.getBadgesEarned().size() : 0);
            stats.put("level", calculateLevel(gamification.getTotalPoints()));
        } else {
            stats.put("totalPoints", 0);
            stats.put("impactScore", 0.0);
            stats.put("badgesEarned", 0);
            stats.put("level", "Beginner");
        }
        
        // Add unique schools count
        stats.put("uniqueSchools", getUniqueSchoolsCountByDonor(donorId));
        
        return stats;
    }

    private String calculateLevel(Integer totalPoints) {
        if (totalPoints == null || totalPoints == 0) return "Beginner";
        if (totalPoints >= 50000) return "Diamond";
        if (totalPoints >= 25000) return "Platinum";
        if (totalPoints >= 10000) return "Gold";
        if (totalPoints >= 2500) return "Silver";
        return "Bronze";
    }

    private DonorGamificationDto convertToDto(DonorGamification gamification) {
        return DonorGamificationDto.builder()
                .gamificationId(gamification.getGamificationId())
                .donorId(gamification.getDonor().getDonorId())
                .totalPoints(gamification.getTotalPoints())
                .impactScore(gamification.getImpactScore())
                .badgesEarned(gamification.getBadgesEarned())
                .donorName(gamification.getDonorName())
                .lastUpdated(gamification.getLastUpdated())
                .build();
    }
}