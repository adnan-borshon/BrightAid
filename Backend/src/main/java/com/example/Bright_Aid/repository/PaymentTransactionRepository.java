package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.PaymentTransaction;
import com.example.Bright_Aid.Entity.PaymentTransaction.TransactionType;
import com.example.Bright_Aid.Entity.PaymentTransaction.PaymentMethod;
import com.example.Bright_Aid.Entity.PaymentTransaction.TransactionStatus;
import com.example.Bright_Aid.Entity.Donor;
import com.example.Bright_Aid.Entity.Donation;
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

}