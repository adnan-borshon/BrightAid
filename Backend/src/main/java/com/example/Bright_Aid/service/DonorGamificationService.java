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
                .rankingPosition(dto.getRankingPositionRequest())
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
        if (dto.getRankingPositionRequest() != null) gamification.setRankingPosition(dto.getRankingPositionRequest());

        gamification.setLastUpdated(LocalDateTime.now());

        return toDto(gamificationRepository.save(gamification));
    }

    private DonorGamificationDto toDto(DonorGamification entity) {
        return DonorGamificationDto.builder()
                .gamificationId(entity.getGamificationId())
                .donorId(entity.getDonor().getDonorId())
                .donorName(entity.getDonorName())
                .organizationName(entity.getDonor().getDonorName())
                .totalPoints(entity.getTotalPoints())
                .currentLevel(entity.getCurrentLevel())
                .badgesEarned(entity.getBadgesEarned())
                .rankingPosition(entity.getRankingPosition())
                .lastUpdated(entity.getLastUpdated())
                .build();
    }

    @Transactional
    public void deleteGamification(Integer id) {
        gamificationRepository.deleteById(id);
    }
}
