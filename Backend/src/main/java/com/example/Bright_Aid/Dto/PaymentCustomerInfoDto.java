package com.example.Bright_Aid.Dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentCustomerInfoDto {

    private Integer customerInfoId;

    private Integer transactionId;

    private String customerName;

    private String customerEmail;

    private String customerPhone;
}
