package com.example.Bright_Aid.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment_customer_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentCustomerInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_info_id")
    private Integer customerInfoId;

    // 1-to-1 with payment_transactions
    @OneToOne
    @JoinColumn(name = "transaction_id", referencedColumnName = "transaction_id", nullable = false, unique = true)
    private PaymentTransaction transaction;

    @Column(name = "customer_name", length = 255)
    private String customerName;

    @Column(name = "customer_email", length = 255)
    private String customerEmail;

    @Column(name = "customer_phone", length = 20)
    private String customerPhone;
}
