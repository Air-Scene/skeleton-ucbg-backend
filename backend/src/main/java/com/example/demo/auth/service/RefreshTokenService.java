package com.example.demo.auth.service;

import com.example.demo.auth.exception.TokenRefreshException;
import com.example.demo.auth.model.RefreshToken;
import com.example.demo.auth.repository.RefreshTokenRepository;
import com.example.demo.account.model.Account;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class RefreshTokenService {

    @Value("${app.jwt.refresh-token.expiration}")
    private Long refreshTokenExpiration;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public RefreshToken createRefreshToken(Account account) {
        // First, delete any existing refresh tokens for this account
        try {
            refreshTokenRepository.deleteByAccount(account);
            log.debug("Deleted existing refresh tokens for account: {}", account.getEmail());
        } catch (Exception e) {
            log.warn("Error while deleting existing refresh tokens for account: {}", account.getEmail(), e);
        }

        // Create and save new refresh token
        RefreshToken refreshToken = RefreshToken.builder()
            .account(account)
            .token(UUID.randomUUID().toString())
            .expiryDate(Instant.now().plusMillis(refreshTokenExpiration))
            .build();

        try {
            return refreshTokenRepository.save(refreshToken);
        } catch (Exception e) {
            log.error("Error while creating refresh token for account: {}", account.getEmail(), e);
            throw new TokenRefreshException("Could not create refresh token");
        }
    }

    public RefreshToken findByRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token)
            .orElseThrow(() -> new TokenRefreshException("Refresh token not found"));
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException("Refresh token was expired");
        }
        return token;
    }

    @Transactional
    public void deleteByRefreshToken(String token) {
        refreshTokenRepository.findByToken(token)
            .ifPresent(refreshTokenRepository::delete);
    }

    @Transactional
    public void deleteByAccount(Account account) {
        try {
            refreshTokenRepository.deleteByAccount(account);
            log.debug("Successfully deleted refresh tokens for account: {}", account.getEmail());
        } catch (Exception e) {
            log.error("Error deleting refresh tokens for account: {}", account.getEmail(), e);
        }
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000) // Run once a day
    @Transactional
    public void cleanupExpiredTokens() {
        Instant now = Instant.now();
        log.info("Cleaning up expired refresh tokens");
        try {
            refreshTokenRepository.deleteByExpiryDateBefore(now);
            log.info("Expired refresh tokens cleanup completed");
        } catch (Exception e) {
            log.error("Error cleaning up expired refresh tokens", e);
        }
    }

    @Transactional
    public void revokeAllUserTokens(Account account) {
        log.info("Revoking all refresh tokens for account: {}", account.getEmail());
        try {
            refreshTokenRepository.deleteByAccount(account);
            log.debug("Successfully revoked all refresh tokens for account: {}", account.getEmail());
        } catch (Exception e) {
            log.error("Error revoking refresh tokens for account: {}", account.getEmail(), e);
        }
    }
} 