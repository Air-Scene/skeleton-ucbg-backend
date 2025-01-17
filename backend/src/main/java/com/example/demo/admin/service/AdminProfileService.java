package com.example.demo.admin.service;

import com.example.demo.admin.dto.AdminProfileDto;
import com.example.demo.admin.dto.CreateAdminProfileDto;
import com.example.demo.admin.dto.UpdateAdminProfileDto;
import com.example.demo.admin.model.AdminProfile;
import com.example.demo.admin.repository.AdminProfileRepository;
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
public class AdminProfileService {
    private final AdminProfileRepository adminProfileRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public AdminProfileDto findById(UUID id) {
        AdminProfile profile = adminProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Admin profile not found"));
        return modelMapper.map(profile, AdminProfileDto.class);
    }

    @Transactional(readOnly = true)
    public AdminProfileDto findByAccountId(UUID accountId) {
        AdminProfile profile = adminProfileRepository.findByAccountId(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Admin profile not found"));
        return modelMapper.map(profile, AdminProfileDto.class);
    }

    @Transactional
    public AdminProfileDto create(UUID accountId, CreateAdminProfileDto createDto) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        if (adminProfileRepository.existsByAccount(account)) {
            throw new IllegalStateException("Admin profile already exists for this account");
        }

        AdminProfile adminProfile = AdminProfile.builder()
            .department(createDto.getDepartment())
            .position(createDto.getPosition())
            .adminLevel(createDto.getAdminLevel())
            .directReportsCount(createDto.getDirectReportsCount())
            .lastAccessDate(OffsetDateTime.now())
            .build();
        
        adminProfile.setAccount(account);
        account.setAdminProfile(adminProfile);
        
        adminProfile = adminProfileRepository.saveAndFlush(adminProfile);
        
        return modelMapper.map(adminProfile, AdminProfileDto.class);
    }

    @Transactional
    public AdminProfileDto update(UUID id, UpdateAdminProfileDto updateDto) {
        AdminProfile profile = adminProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Admin profile not found"));

        if (updateDto.getDepartment() != null) profile.setDepartment(updateDto.getDepartment());
        if (updateDto.getPosition() != null) profile.setPosition(updateDto.getPosition());
        if (updateDto.getAdminLevel() != null) profile.setAdminLevel(updateDto.getAdminLevel());
        if (updateDto.getDirectReportsCount() != null) profile.setDirectReportsCount(updateDto.getDirectReportsCount());

        profile = adminProfileRepository.save(profile);
        return modelMapper.map(profile, AdminProfileDto.class);
    }

    @Transactional
    public void delete(UUID id) {
        if (!adminProfileRepository.existsById(id)) {
            throw new EntityNotFoundException("Admin profile not found");
        }
        adminProfileRepository.deleteById(id);
    }

    @Transactional
    public void updateLastAccessDate(UUID id) {
        AdminProfile profile = adminProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Admin profile not found"));
        
        profile.setLastAccessDate(OffsetDateTime.now());
        adminProfileRepository.save(profile);
    }
} 