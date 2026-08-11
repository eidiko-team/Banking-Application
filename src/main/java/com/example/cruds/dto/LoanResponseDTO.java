package com.example.cruds.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanResponseDTO {

    private Long loanId;

    private String loanType;

    private Double loanAmount;

    private Double interestRate;

    private Integer tenureMonths;

    private String status;

    private Long customerId;
}