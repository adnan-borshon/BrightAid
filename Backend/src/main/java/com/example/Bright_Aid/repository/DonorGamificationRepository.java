package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.DonorGamification;
import com.example.Bright_Aid.Entity.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonorGamificationRepository extends JpaRepository<DonorGamification, Integer> {

    Optional<DonorGamification> findByDonor(Donor donor);

    Optional<DonorGamification> findByDonor_DonorId(Integer donorId);

    List<DonorGamification> findByCurrentLevel(String currentLevel);

    List<DonorGamification> findByTotalPointsGreaterThanEqual(Integer points);

    @Query("SELECT dg FROM DonorGamification dg ORDER BY dg.totalPoints DESC")
    List<DonorGamification> findAllOrderByTotalPointsDesc();

    @Query("SELECT dg FROM DonorGamification dg WHERE dg.rankingPosition <= :position ORDER BY dg.rankingPosition")
    List<DonorGamification> findTopRankedDonors(@Param("position") Integer position);
}