package com.example.cruds.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//
//{
//        "loanType": "Home Loan",
//        "loanAmount": 2000000,
//        "interestRate": 8.5,
//        "tenureMonths": 240,
//        "status": "ACTIVE",
//        "customerId": 1
//        }

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanRequestDTO {

    private String loanType;

    private Double loanAmount;

    private Double interestRate;

    private Integer tenureMonths;

    private String status;

    private Long customerId;
}