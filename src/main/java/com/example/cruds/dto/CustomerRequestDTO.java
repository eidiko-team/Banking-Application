package com.example.cruds.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequestDTO {

    @NotNull(message = "Name should be given")
    @Size(min = 5, max = 40,
            message = "Name must be between 5 and 40 characters")
    private String name;

    @Email(message = "Email must be valid")
    private String email;

    @Size(max = 10)
    private String phone;

    @NotBlank(message = "Address is required")
    @Size(min = 10, max = 100,
            message = "Address must be between 10 and 100 characters")
    private String address;
}