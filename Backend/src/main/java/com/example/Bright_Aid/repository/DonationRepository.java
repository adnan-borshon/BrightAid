package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Integer> {

    // Find donations by donor ID
    List<Donation> findByDonor_DonorId(Integer donorId);

    // Find donations by project ID
    List<Donation> findByProject_ProjectId(Integer projectId);

    // Find donations by student ID
    List<Donation> findByStudent_StudentId(Integer studentId);

    // Find donations by payment status
    List<Donation> findByPaymentStatus(Donation.PaymentStatus paymentStatus);

    // Custom query to get donor statistics
    @Query("SELECT SUM(d.amount) FROM Donation d WHERE d.donor.donorId = :donorId AND d.paymentStatus = 'COMPLETED'")
    BigDecimal getTotalDonatedByDonor(@Param("donorId") Integer donorId);

}