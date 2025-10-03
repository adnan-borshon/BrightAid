package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Entity.NgoGamification;
import com.example.Bright_Aid.Dto.NgoGamificationDTO;
import com.example.Bright_Aid.repository.NgoGamificationRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NgoGamificationService {

    private final NgoGamificationRepository repository;

    public NgoGamificationService(NgoGamificationRepository repository) {
        this.repository = repository;
    }

    // ===================== CREATE =====================
    public NgoGamificationDTO create(NgoGamificationDTO dto) {
        // Check if NGO gamification already exists
        if (repository.existsByNgoId(dto.getNgoId())) {
            throw new RuntimeException("NGO Gamification already exists for NGO ID: " + dto.getNgoId());
        }
        
        NgoGamification entity = mapToEntity(dto);

        // Auto-calculate gamification metrics
        calculateGamificationMetrics(entity);
        entity.setLastUpdated(LocalDateTime.now());

        NgoGamification saved = repository.save(entity);
        return mapToDTO(saved);
    }

    // ===================== UPDATE =====================
    public NgoGamificationDTO update(Integer id, NgoGamificationDTO dto) {
        NgoGamification entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("NGO Gamification not found"));

        entity.setNgoName(dto.getNgoName());
        entity.setNgoId(dto.getNgoId());
        
        // Recalculate metrics on update
        calculateGamificationMetrics(entity);
        entity.setLastUpdated(LocalDateTime.now());

        NgoGamification updated = repository.save(entity);
        return mapToDTO(updated);
    }

    // ===================== GET BY ID =====================
    public NgoGamificationDTO getById(Integer id) {
        NgoGamification entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("NGO Gamification not found"));
        return mapToDTO(entity);
    }

    // ===================== GET ALL =====================
    public List<NgoGamificationDTO> getAll() {
        return repository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ===================== DELETE =====================
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    // ===================== MAPPER METHODS =====================
    private NgoGamificationDTO mapToDTO(NgoGamification entity) {
        return NgoGamificationDTO.builder()
                .gamificationId(entity.getGamificationId())
                .ngoName(entity.getNgoName())
                .ngoId(entity.getNgoId())
                .badgesEarned(entity.getBadgesEarned())
                .lastUpdated(entity.getLastUpdated())
                .totalPoints(entity.getTotalPoints())
                .impactScore(entity.getImpactScore())
                .build();
    }

    private NgoGamification mapToEntity(NgoGamificationDTO dto) {
        return NgoGamification.builder()
                .ngoName(dto.getNgoName())
                .ngoId(dto.getNgoId())
                .build();
    }

    // ===================== GAMIFICATION CALCULATION =====================
    private void calculateGamificationMetrics(NgoGamification entity) {
        // Calculate total points based on NGO activities
        int totalPoints = calculateTotalPoints(entity.getNgoId());
        entity.setTotalPoints(totalPoints);

        // Calculate impact score (0.0 to 10.0)
        Double impactScore = calculateImpactScore(entity.getNgoId(), totalPoints);
        entity.setImpactScore(BigDecimal.valueOf(impactScore));

        // Generate badges based on achievements
        String badges = generateBadges(totalPoints, impactScore);
        entity.setBadgesEarned(badges);
    }

    private int calculateTotalPoints(Integer ngoId) {
        // Points calculation logic:
        // - 100 points per completed project
        // - 50 points per active project
        // - 10 points per school helped
        // - 5 points per student impacted
        
        int points = 0;
        
        // Base points for having an NGO profile
        points += 50;
        
        // Simulate project-based points (you can replace with actual queries)
        // Example: points += completedProjects * 100 + activeProjects * 50
        points += (ngoId % 5) * 100; // Completed projects simulation
        points += (ngoId % 3) * 50;  // Active projects simulation
        points += (ngoId % 10) * 10; // Schools helped simulation
        points += (ngoId % 20) * 5;  // Students impacted simulation
        
        return Math.max(points, 50); // Minimum 50 points
    }

    private Double calculateImpactScore(Integer ngoId, int totalPoints) {
        // Impact score based on:
        // - Total points achieved
        // - Consistency of activities
        // - Geographic reach
        // - Student success rate
        
        double baseScore = Math.min(totalPoints / 100.0, 8.0); // Max 8 from points
        double bonusScore = (ngoId % 7) * 0.3; // Bonus for consistency/reach
        
        double impactScore = baseScore + bonusScore;
        return Math.min(Math.round(impactScore * 10.0) / 10.0, 10.0); // Round to 1 decimal, max 10.0
    }

    private String generateBadges(int totalPoints, Double impactScore) {
        StringBuilder badges = new StringBuilder("[");
        boolean hasBadge = false;
        
        // Points-based badges
        if (totalPoints >= 1000) {
            badges.append("Champion");
            hasBadge = true;
        } else if (totalPoints >= 500) {
            badges.append("Expert");
            hasBadge = true;
        } else if (totalPoints >= 200) {
            badges.append("Achiever");
            hasBadge = true;
        } else if (totalPoints >= 100) {
            badges.append("Starter");
            hasBadge = true;
        }
        
        // Impact-based badges
        if (impactScore >= 9.0) {
            if (hasBadge) badges.append(",");
            badges.append("\"High Impact\"");
            hasBadge = true;
        } else if (impactScore >= 7.0) {
            if (hasBadge) badges.append(",");
            badges.append("\"Good Impact\"");
            hasBadge = true;
        }
        
        // Special badges
        if (totalPoints >= 300 && impactScore >= 6.0) {
            if (hasBadge) badges.append(",");
            badges.append("\"Consistent Performer\"");
        }
        
        badges.append("]");
        return badges.toString();
    }
}
