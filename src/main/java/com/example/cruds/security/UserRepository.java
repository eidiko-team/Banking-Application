package com.example.cruds.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository
        extends JpaRepository<User, Long> {

    Optional<User> findByCustomerCustomerId(Long customerId);

    Optional<User> findByCustomerEmail(String email);  //searches for a User whose associated customer's email
}