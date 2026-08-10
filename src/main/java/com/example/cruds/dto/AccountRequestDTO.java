package com.example.cruds.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequestDTO {

    private String accountNumber;
    private String accountType;
    private Double balance;
    private String status;

    private Long customerId;
}
