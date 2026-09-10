package com.example.cruds.security;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        String username,
        String password
) {
}