package com.example.cruds.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerLoanProductResponseDTO {

    private Long productId;

    private String productName;

    private Double interestRate;

    private Integer maxTenureMonths;
}