package com.example.cruds.security;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;


    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterRequest request) {

        return authService.register(request);
    }

    @PostMapping("/refresh")
    public String refresh(@RequestBody RefreshTokenRequest request) {

        return authService.refreshAccessToken(request.getRefreshToken());
    }


    @PostMapping("/generateToken")
    public LoginResponse authenticateAndGetToken(@Valid @RequestBody RegisterRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        if (authentication.isAuthenticated()) {
            String refreshToken = jwtService.generateRefreshToken(request.username());

            String accessToken = jwtService.generateAccessToken(request.username());

            return new LoginResponse(accessToken,refreshToken);

        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }



}
