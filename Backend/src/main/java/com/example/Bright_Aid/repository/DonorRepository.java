package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface DonorRepository extends JpaRepository<Donor, Integer> {

    // 1. Find donor by userId
    @Query("SELECT d FROM Donor d WHERE d.user.userId = :userId")
    Optional<Donor> findByUserId(@Param("userId") Integer userId);

    // 2. Get all anonymous donors
    @Query("SELECT d FROM Donor d WHERE d.isAnonymous = true")
    List<Donor> findAllAnonymousDonors();

    // 3. Get donors who donated more than X
    @Query("SELECT d FROM Donor d WHERE d.totalDonated > :amount")
    List<Donor> findByTotalDonatedGreaterThan(@Param("amount") BigDecimal amount);



    // 5. Top N donors by totalDonated
    @Query("SELECT d FROM Donor d ORDER BY d.totalDonated DESC")
    List<Donor> findTopDonors(org.springframework.data.domain.Pageable pageable);

}
