package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.PaymentTransaction;
import com.example.Bright_Aid.Entity.PaymentTransaction.TransactionStatus;
import com.example.Bright_Aid.Entity.PaymentTransaction.TransactionType;
import com.example.Bright_Aid.Entity.PaymentTransaction.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Integer> {

    // 🔹 Find by unique transaction reference
    @Query(value = "SELECT * FROM payment_transactions WHERE transaction_reference = :reference", nativeQuery = true)
    Optional<PaymentTransaction> findByTransactionReference(@Param("reference") String reference);

    // 🔹 Find all transactions by donor
    @Query(value = "SELECT * FROM payment_transactions WHERE donor_id = :donorId", nativeQuery = true)
    List<PaymentTransaction> findByDonorId(@Param("donorId") Integer donorId);

    // 🔹 Find all transactions by donation
    @Query(value = "SELECT * FROM payment_transactions WHERE donation_id = :donationId", nativeQuery = true)
    List<PaymentTransaction> findByDonationId(@Param("donationId") Integer donationId);

    // 🔹 Find transactions by status
    @Query(value = "SELECT * FROM payment_transactions WHERE status = :status", nativeQuery = true)
    List<PaymentTransaction> findByStatus(@Param("status") String status);

    // 🔹 Find transactions by type (DONATION / REFUND)
    @Query(value = "SELECT * FROM payment_transactions WHERE transaction_type = :type", nativeQuery = true)
    List<PaymentTransaction> findByTransactionType(@Param("type") String type);

    // 🔹 Find transactions by payment method
    @Query(value = "SELECT * FROM payment_transactions WHERE payment_method = :method", nativeQuery = true)
    List<PaymentTransaction> findByPaymentMethod(@Param("method") String method);

    // 🔹 Find transactions greater than a certain amount
    @Query(value = "SELECT * FROM payment_transactions WHERE amount > :amount", nativeQuery = true)
    List<PaymentTransaction> findByAmountGreaterThan(@Param("amount") BigDecimal amount);

    // 🔹 Find transactions between two dates (initiated_at)
    @Query(value = "SELECT * FROM payment_transactions WHERE initiated_at BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<PaymentTransaction> findByInitiatedAtBetween(@Param("startDate") LocalDateTime startDate,
                                                     @Param("endDate") LocalDateTime endDate);

    // 🔹 Find successful transactions for a donor
    @Query(value = "SELECT * FROM payment_transactions WHERE donor_id = :donorId AND status = 'SUCCESS'", nativeQuery = true)
    List<PaymentTransaction> findSuccessfulTransactionsByDonor(@Param("donorId") Integer donorId);
}
