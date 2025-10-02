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

    // Find donations by donor ID ordered by date (recent first) with transaction details
    @Query(value = "SELECT d.donation_id, d.donor_id, d.project_id, d.student_id, d.amount, " +
           "d.donation_type, d.transaction_id, d.payment_status, d.purpose, d.donor_message, " +
           "d.is_anonymous, d.donated_at, d.payment_completed_at, d.created_at, d.updated_at, " +
           "COALESCE(pt.transaction_reference, CONCAT('TXN', LPAD(d.donation_id, 9, '0'))) as transaction_ref, " +
           "COALESCE(sp.project_title, CONCAT('Donation #', d.donation_id)) as project_name " +
           "FROM donations d " +
           "LEFT JOIN payment_transactions pt ON d.transaction_id = pt.transaction_id " +
           "LEFT JOIN school_projects sp ON d.project_id = sp.project_id " +
           "WHERE d.donor_id = :donorId " +
           "ORDER BY COALESCE(d.donated_at, d.created_at) DESC", nativeQuery = true)
    List<Object[]> findDonationsByDonorWithDetailsOrderByDateDesc(@Param("donorId") Integer donorId);

}