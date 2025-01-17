package com.example.demo.account.service;

import com.example.demo.account.dto.AccountDto;
import com.example.demo.account.dto.UpdateAccountDto;
import com.example.demo.account.model.Account;
import com.example.demo.account.model.AccountRole;
import com.example.demo.account.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.BadCredentialsException;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<AccountDto> findAll() {
        return accountRepository.findAll().stream()
                .map(AccountDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AccountDto findById(UUID id) {
        return AccountDto.fromEntity(findAccountById(id));
    }

    private Account findAccountById(UUID id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
    }

    @Transactional(readOnly = true)
    public AccountDto findByEmail(String email) {
        return accountRepository.findByEmail(email)
                .map(AccountDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
    }

    @Transactional
    public AccountDto create(Account account) {
        if (accountRepository.existsByEmail(account.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        account.setPasswordHash(passwordEncoder.encode(account.getPasswordHash()));
        return AccountDto.fromEntity(accountRepository.save(account));
    }

    @Transactional
    public void delete(UUID id) {
        if (!accountRepository.existsById(id)) {
            throw new EntityNotFoundException("Account not found");
        }
        accountRepository.deleteById(id);
    }

    @Transactional
    public AccountDto updateRole(UUID id, AccountRole role) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        account.setRole(role);
        return AccountDto.fromEntity(accountRepository.save(account));
    }

    @Transactional
    public void updatePassword(UUID accountId, String currentPassword, String newPassword) {
        Account account = getAccount(accountId);

        if (!passwordEncoder.matches(currentPassword, account.getPasswordHash())) {
            throw new BadCredentialsException("Current password is incorrect");
        }

        account.setPasswordHash(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public AccountDto getAccountDto(UUID id) {
        return AccountDto.fromEntity(getAccount(id));
    }

    // Internal use only - returns full entity with sensitive data
    private Account getAccount(UUID id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
    }

    @Transactional
    public AccountDto update(UUID id, UpdateAccountDto updateDto) {
        log.info("Updating account with id: {}", id);
        log.info("UpdatedAccount: {}", updateDto);
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        if (updateDto.getEmail() != null) {
            if (!account.getEmail().equals(updateDto.getEmail()) && 
                accountRepository.existsByEmail(updateDto.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
            account.setEmail(updateDto.getEmail());
        }
        if (updateDto.getFirstName() != null) {
            account.setFirstName(updateDto.getFirstName());
        }
        if (updateDto.getLastName() != null) {
            account.setLastName(updateDto.getLastName());
        }
        if (updateDto.getLanguage() != null) {
            account.setLanguage(updateDto.getLanguage());
        }
        log.info("Updated account: {}", account);

        return AccountDto.fromEntity(accountRepository.save(account));
    }
} 