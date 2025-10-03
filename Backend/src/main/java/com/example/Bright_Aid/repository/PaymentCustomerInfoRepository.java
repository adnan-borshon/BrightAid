package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.PaymentCustomerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentCustomerInfoRepository extends JpaRepository<PaymentCustomerInfo, Integer> {

    // Find by transactionId
    @Query(value = "SELECT * FROM payment_customer_info WHERE transaction_id = :transactionId", nativeQuery = true)
    PaymentCustomerInfo findByTransactionId(Integer transactionId);

    // Find by email
    @Query(value = "SELECT * FROM payment_customer_info WHERE customer_email = :email", nativeQuery = true)
    PaymentCustomerInfo findByCustomerEmail(String email);

    // Find by phone
    @Query(value = "SELECT * FROM payment_customer_info WHERE customer_phone = :phone", nativeQuery = true)
    PaymentCustomerInfo findByCustomerPhone(String phone);
}
