package com.example.demo.account.controller;

import com.example.demo.account.dto.AccountDto;
import com.example.demo.account.dto.UpdateAccountDto;
import com.example.demo.account.dto.UpdatePasswordDto;
import com.example.demo.account.model.Account;
import com.example.demo.account.model.AccountRole;
import com.example.demo.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.demo.auth.model.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        return ResponseEntity.ok(accountService.findAll());
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AccountDto> getCurrentAccount(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(accountService.getAccountDto(userPrincipal.getId()));
    }

    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AccountDto> updateCurrentAccount(
        @AuthenticationPrincipal UserPrincipal userPrincipal,
        @Valid @RequestBody UpdateAccountDto updateDto
    ) {
        return ResponseEntity.ok(accountService.update(userPrincipal.getId(), updateDto));
    }

    @PutMapping("/me/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> updateCurrentAccountPassword(
        @AuthenticationPrincipal UserPrincipal userPrincipal,
        @Valid @RequestBody UpdatePasswordDto updatePasswordDto
    ) {
        accountService.updatePassword(userPrincipal.getId(), 
            updatePasswordDto.getCurrentPassword(), 
            updatePasswordDto.getNewPassword());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable UUID id) {
        return ResponseEntity.ok(accountService.getAccountDto(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AccountDto> createAccount(@Valid @RequestBody Account account) {
        return ResponseEntity.ok(accountService.create(account));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AccountDto> updateAccount(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateAccountDto updateDto) {
        return ResponseEntity.ok(accountService.update(id, updateDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAccount(@PathVariable UUID id) {
        accountService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AccountDto> updateRole(
            @PathVariable UUID id,
            @RequestBody AccountRole role) {
        return ResponseEntity.ok(accountService.updateRole(id, role));
    }
} 