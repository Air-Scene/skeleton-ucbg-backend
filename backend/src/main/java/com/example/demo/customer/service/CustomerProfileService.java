package com.example.demo.customer.service;

import com.example.demo.customer.dto.CustomerProfileDto;
import com.example.demo.customer.dto.CreateCustomerProfileDto;
import com.example.demo.customer.dto.UpdateCustomerProfileDto;
import com.example.demo.customer.model.CustomerProfile;
import com.example.demo.customer.repository.CustomerProfileRepository;
import com.example.demo.account.model.Account;
import com.example.demo.account.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerProfileService {
    private final CustomerProfileRepository customerProfileRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public CustomerProfileDto findById(UUID id) {
        CustomerProfile profile = customerProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer profile not found"));
        return modelMapper.map(profile, CustomerProfileDto.class);
    }

    @Transactional(readOnly = true)
    public CustomerProfileDto findByAccountId(UUID accountId) {
        CustomerProfile profile = customerProfileRepository.findByAccountId(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Customer profile not found"));
        return modelMapper.map(profile, CustomerProfileDto.class);
    }

    @Transactional
    public CustomerProfileDto create(UUID accountId, CreateCustomerProfileDto createDto) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        if (customerProfileRepository.existsByAccount(account)) {
            throw new IllegalStateException("Customer profile already exists for this account");
        }

        CustomerProfile profile = modelMapper.map(createDto, CustomerProfile.class);
        profile.setAccount(account);
        profile.setLastPurchaseDate(OffsetDateTime.now());

        profile = customerProfileRepository.save(profile);
        return modelMapper.map(profile, CustomerProfileDto.class);
    }

    @Transactional
    public CustomerProfileDto update(UUID id, UpdateCustomerProfileDto updateDto) {
        CustomerProfile profile = customerProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer profile not found"));

        if (updateDto.getCompanyName() != null) profile.setCompanyName(updateDto.getCompanyName());
        if (updateDto.getIndustry() != null) profile.setIndustry(updateDto.getIndustry());
        if (updateDto.getPhoneNumber() != null) profile.setPhoneNumber(updateDto.getPhoneNumber());
        if (updateDto.getAddress() != null) profile.setAddress(updateDto.getAddress());
        if (updateDto.getCity() != null) profile.setCity(updateDto.getCity());
        if (updateDto.getCountry() != null) profile.setCountry(updateDto.getCountry());
        if (updateDto.getPostalCode() != null) profile.setPostalCode(updateDto.getPostalCode());
        if (updateDto.getVatNumber() != null) profile.setVatNumber(updateDto.getVatNumber());

        profile = customerProfileRepository.save(profile);
        return modelMapper.map(profile, CustomerProfileDto.class);
    }

    @Transactional
    public void delete(UUID id) {
        if (!customerProfileRepository.existsById(id)) {
            throw new EntityNotFoundException("Customer profile not found");
        }
        customerProfileRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public int count() {
        return (int) customerProfileRepository.count();
    }
} 