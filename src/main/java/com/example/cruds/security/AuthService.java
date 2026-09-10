package com.example.cruds.security;

import com.example.cruds.models.Customer;
import com.example.cruds.repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public String register(RegisterRequest request) {

        // 1. Find existing customer using email
        Customer customer = customerRepo
                .findByEmail(request.username())
                .orElseThrow(() ->
                        new RuntimeException("Customer not found"));

        // 2. Check whether customer already registered
        if (userRepository.findByCustomerEmail(request.username()).isPresent()) {
            throw new RuntimeException(
                    "Customer is already registered");
        }

        // 3. Create security user
        User user = new User();

        // 4. Encode password
        user.setPassword(passwordEncoder.encode(request.password()));

        // 5. Assign role
        user.setRole("CUSTOMER");

        // 6. Connect User with Customer
        user.setCustomer(customer);

        // 7. Save User
        userRepository.save(user);

        return "Registration successful";
    }

    public String refreshAccessToken(String refreshToken) {

        String username = jwtService.extractUsername(refreshToken);

        String tokenType = jwtService.extractTokenType(refreshToken);

        if (!"refresh".equals(tokenType)) {
            throw new RuntimeException("Invalid refresh token");
        }

        return jwtService.generateAccessToken(username);
    }
}