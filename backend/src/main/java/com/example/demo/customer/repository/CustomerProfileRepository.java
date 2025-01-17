package com.example.demo.customer.repository;

import com.example.demo.customer.model.CustomerProfile;
import com.example.demo.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, UUID> {
    Optional<CustomerProfile> findByAccount(Account account);
    Optional<CustomerProfile> findByAccountId(UUID accountId);
    boolean existsByAccount(Account account);
    void deleteByAccount(Account account);
} 