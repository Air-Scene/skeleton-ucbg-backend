package com.example.demo.user.repository;

import com.example.demo.user.model.UserProfile;
import com.example.demo.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
    Optional<UserProfile> findByAccount(Account account);
    Optional<UserProfile> findByAccountId(UUID accountId);
    boolean existsByAccount(Account account);
} 