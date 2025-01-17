package com.example.demo.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
