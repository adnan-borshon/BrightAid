package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Integer> {
    
    Optional<PaymentTransaction> findByTransactionReference(String transactionReference);
    Optional<PaymentTransaction> findBySessionKey(String sessionKey);
}