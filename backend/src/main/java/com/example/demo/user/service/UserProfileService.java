package com.example.demo.user.service;

import com.example.demo.user.dto.UserProfileDto;
import com.example.demo.user.dto.CreateUserProfileDto;
import com.example.demo.user.dto.UpdateUserProfileDto;
import com.example.demo.user.model.UserProfile;
import com.example.demo.user.repository.UserProfileRepository;
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
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public UserProfileDto findById(UUID id) {
        UserProfile profile = userProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User profile not found"));
        return modelMapper.map(profile, UserProfileDto.class);
    }

    @Transactional(readOnly = true)
    public UserProfileDto findByAccountId(UUID accountId) {
        UserProfile profile = userProfileRepository.findByAccountId(accountId)
                .orElseThrow(() -> new EntityNotFoundException("User profile not found"));
        return modelMapper.map(profile, UserProfileDto.class);
    }

    @Transactional
    public UserProfileDto create(UUID accountId, CreateUserProfileDto createDto) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        if (userProfileRepository.existsByAccount(account)) {
            throw new IllegalStateException("User profile already exists for this account");
        }

        UserProfile profile = modelMapper.map(createDto, UserProfile.class);
        profile.setAccount(account);
        profile.setLastLoginDate(OffsetDateTime.now());

        profile = userProfileRepository.save(profile);
        return modelMapper.map(profile, UserProfileDto.class);
    }

    @Transactional
    public void delete(UUID id) {
        if (!userProfileRepository.existsById(id)) {
            throw new EntityNotFoundException("User profile not found");
        }
        userProfileRepository.deleteById(id);
    }

    @Transactional
    public void updateLastLoginDate(UUID id) {
        UserProfile profile = userProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User profile not found"));
        
        profile.setLastLoginDate(OffsetDateTime.now());
        userProfileRepository.save(profile);
    }

    @Transactional
    public UserProfileDto update(UUID id, UpdateUserProfileDto updateDto) {
        UserProfile profile = userProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User profile not found"));

        if (updateDto.getBio() != null) profile.setBio(updateDto.getBio());
        if (updateDto.getPhoneNumber() != null) profile.setPhoneNumber(updateDto.getPhoneNumber());
        if (updateDto.getAddress() != null) profile.setAddress(updateDto.getAddress());
        if (updateDto.getCity() != null) profile.setCity(updateDto.getCity());
        if (updateDto.getCountry() != null) profile.setCountry(updateDto.getCountry());
        if (updateDto.getPostalCode() != null) profile.setPostalCode(updateDto.getPostalCode());

        profile = userProfileRepository.save(profile);
        return modelMapper.map(profile, UserProfileDto.class);
    }

    @Transactional(readOnly = true)
    public int count() {
        return (int) userProfileRepository.count();
    }
} 