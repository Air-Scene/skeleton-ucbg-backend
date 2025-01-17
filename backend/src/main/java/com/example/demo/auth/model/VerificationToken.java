package com.example.demo.auth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import com.example.demo.account.model.Account;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "verification_tokens", schema = "app")
public class VerificationToken {

    private static final int EXPIRATION_HOURS = 24;

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    // Both @NotNull and nullable=false are used for validation at different levels:
    // - @NotNull: Application-level validation
    // - nullable=false: Database-level constraint
    @NotNull
    @Column(nullable = false, unique = true, length = 100)
    private String token;

    // User relationship is LAZY to prevent unnecessary database queries
    // @ToString.Exclude prevents LazyInitializationException and infinite recursion
    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    @ToString.Exclude
    private Account account;

    @NotNull
    @Column(name = "expiry_date", nullable = false)
    private Instant expiryDate;        // Using Instant for timezone-independent expiry

    // Audit fields for tracking creation and updates
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;  // Using OffsetDateTime for audit fields (includes timezone)

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public VerificationToken(String token, Account account) {
        this.token = token;
        this.account = account;
        this.expiryDate = calculateExpiryDate();
    }

    private Instant calculateExpiryDate() {
        return Instant.now().plusSeconds(EXPIRATION_HOURS * 3600);
    }

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiryDate);
    }
} 