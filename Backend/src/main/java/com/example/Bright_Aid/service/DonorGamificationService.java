package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Dto.DonorGamificationDto;
import com.example.Bright_Aid.Entity.DonorGamification;
import com.example.Bright_Aid.Entity.Donor;
import com.example.Bright_Aid.repository.DonorGamificationRepository;
import com.example.Bright_Aid.repository.DonorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DonorGamificationService {

    private final DonorGamificationRepository donorGamificationRepository;
    private final DonorRepository donorRepository;

    public DonorGamificationDto createGamification(DonorGamificationDto dto) {
        Donor donor = donorRepository.findById(dto.getDonorId())
                .orElseThrow(() -> new RuntimeException("Donor not found with ID: " + dto.getDonorId()));

        DonorGamification gamification = DonorGamification.builder()
                .donor(donor)
                .totalPoints(dto.getTotalPoints())
                .currentLevel(dto.getCurrentLevel())
                .badgesEarned(dto.getBadgesEarned())
                .rankingPosition(dto.getRankingPosition())
                .lastUpdated(LocalDateTime.now())
                .build();

        DonorGamification saved = donorGamificationRepository.save(gamification);
        return convertToDto(saved);
    }

    @Transactional(readOnly = true)
    public DonorGamificationDto getGamificationById(Integer id) {
        DonorGamification gamification = donorGamificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gamification not found with ID: " + id));
        return convertToDto(gamification);
    }

    @Transactional(readOnly = true)
    public DonorGamificationDto getGamificationByDonorId(Integer donorId) {
        DonorGamification gamification = donorGamificationRepository.findByDonor_DonorId(donorId)
                .orElseThrow(() -> new RuntimeException("Gamification not found for donor ID: " + donorId));
        return convertToDto(gamification);
    }

    @Transactional(readOnly = true)
    public List<DonorGamificationDto> getAllGamifications() {
        return donorGamificationRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DonorGamificationDto> getGamificationsByLevel(String level) {
        return donorGamificationRepository.findByCurrentLevel(level)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DonorGamificationDto> getGamificationsByMinPoints(Integer minPoints) {
        return donorGamificationRepository.findByTotalPointsGreaterThanEqual(minPoints)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DonorGamificationDto> getLeaderboard() {
        return donorGamificationRepository.findAllOrderByTotalPointsDesc()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DonorGamificationDto> getTopRankedDonors(Integer topN) {
        return donorGamificationRepository.findTopRankedDonors(topN)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public DonorGamificationDto updateGamification(Integer id, DonorGamificationDto dto) {
        DonorGamification existing = donorGamificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gamification not found with ID: " + id));

        if (dto.getDonorId() != null && !dto.getDonorId().equals(existing.getDonor().getDonorId())) {
            Donor newDonor = donorRepository.findById(dto.getDonorId())
                    .orElseThrow(() -> new RuntimeException("Donor not found with ID: " + dto.getDonorId()));
            existing.setDonor(newDonor);
        }

        if (dto.getTotalPoints() != null) {
            existing.setTotalPoints(dto.getTotalPoints());
        }
        if (dto.getCurrentLevel() != null) {
            existing.setCurrentLevel(dto.getCurrentLevel());
        }
        if (dto.getBadgesEarned() != null) {
            existing.setBadgesEarned(dto.getBadgesEarned());
        }
        if (dto.getRankingPosition() != null) {
            existing.setRankingPosition(dto.getRankingPosition());
        }

        existing.setLastUpdated(LocalDateTime.now());

        DonorGamification updated = donorGamificationRepository.save(existing);
        return convertToDto(updated);
    }

    public DonorGamificationDto addPoints(Integer donorId, Integer points) {
        DonorGamification gamification = donorGamificationRepository.findByDonor_DonorId(donorId)
                .orElseThrow(() -> new RuntimeException("Gamification not found for donor ID: " + donorId));

        gamification.setTotalPoints(gamification.getTotalPoints() + points);
        gamification.setLastUpdated(LocalDateTime.now());

        // You can add level calculation logic here
        updateLevel(gamification);

        DonorGamification updated = donorGamificationRepository.save(gamification);
        return convertToDto(updated);
    }

    public void deleteGamification(Integer id) {
        if (!donorGamificationRepository.existsById(id)) {
            throw new RuntimeException("Gamification not found with ID: " + id);
        }
        donorGamificationRepository.deleteById(id);
    }

    private void updateLevel(DonorGamification gamification) {
        // Simple level calculation based on points
        int points = gamification.getTotalPoints();
        if (points < 100) {
            gamification.setCurrentLevel("Bronze");
        } else if (points < 500) {
            gamification.setCurrentLevel("Silver");
        } else if (points < 1000) {
            gamification.setCurrentLevel("Gold");
        } else {
            gamification.setCurrentLevel("Platinum");
        }
    }

    private DonorGamificationDto convertToDto(DonorGamification entity) {
        DonorGamificationDto.DonorBasicDto donorDto = null;
        if (entity.getDonor() != null) {
            Donor donor = entity.getDonor();
            donorDto = DonorGamificationDto.DonorBasicDto.builder()
                    .donorId(donor.getDonorId())
                    .organizationName(donor.getOrganizationName())
                    .isAnonymous(donor.getIsAnonymous())
                    .userName(donor.getUser() != null ? donor.getUser().getUsername() : null)
                    .userEmail(donor.getUser() != null ? donor.getUser().getEmail() : null)
                    .build();
        }

        return DonorGamificationDto.builder()
                .gamificationId(entity.getGamificationId())
                .donorId(entity.getDonor() != null ? entity.getDonor().getDonorId() : null)
                .totalPoints(entity.getTotalPoints())
                .currentLevel(entity.getCurrentLevel())
                .badgesEarned(entity.getBadgesEarned())
                .rankingPosition(entity.getRankingPosition())
                .lastUpdated(entity.getLastUpdated())
                .donor(donorDto)
                .build();
    }
}