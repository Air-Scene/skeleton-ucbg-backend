package com.example.demo.auth.dto;

import com.example.demo.account.dto.AccountDto;
import com.example.demo.account.model.AccountRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    @Builder.Default
    private String tokenType = "Bearer";
    private AccountDto account;
    private AccountRole role;
    
    public String getEmail() {
        return account != null ? account.getEmail() : null;
    }
}
