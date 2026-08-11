package com.example.cruds.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDTO {

    private Long customerId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private List<AccountResponseDTO> accounts;
    private KycResponseDTO kyc;
    private List<LoanProductResponseDTO> loanOffers;
    private List<LoanResponseDTO> loans;
}