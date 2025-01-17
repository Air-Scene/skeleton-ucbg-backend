package com.example.demo.auth.dto;

import com.example.demo.account.model.AccountRole;
import com.example.demo.account.model.AccountLanguage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private AccountLanguage language = AccountLanguage.de;

    @NotNull(message = "Role is required")
    private AccountRole role = AccountRole.ROLE_USER;
}
