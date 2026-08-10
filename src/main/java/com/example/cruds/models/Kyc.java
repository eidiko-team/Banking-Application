package com.example.cruds.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Kyc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kyc_id;
    private String aadhaarNumber;
    private String panNumber;
    private Boolean verificationStatus;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

}
