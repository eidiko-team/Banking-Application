package com.example.cruds.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class KycResponseDTO {
    private Long kyc_id;
    private String aadhaarNumber;
    private String panNumber;
    private Boolean verificationStatus;
    private Long customerid;
}
