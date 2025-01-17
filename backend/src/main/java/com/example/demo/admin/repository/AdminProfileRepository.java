package com.example.demo.admin.repository;

import com.example.demo.admin.model.AdminProfile;
import com.example.demo.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminProfileRepository extends JpaRepository<AdminProfile, UUID> {
    Optional<AdminProfile> findByAccount(Account account);
    Optional<AdminProfile> findByAccountId(UUID accountId);
    boolean existsByAccount(Account account);
    void deleteByAccount(Account account);
} 