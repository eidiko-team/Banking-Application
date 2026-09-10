package com.example.cruds.security;

import com.example.cruds.models.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String password;

    private String role;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}