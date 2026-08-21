package com.example.cruds.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("STATUS")
    private String status;

    @JsonIgnore
    private Long customerId;
}