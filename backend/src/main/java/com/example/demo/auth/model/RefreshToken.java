package com.example.demo.auth.model;

import com.example.demo.account.model.Account;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
    name = "refresh_tokens", 
    schema = "app",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"token"}),
        @UniqueConstraint(columnNames = {"account_id"})
    }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false)
    private Account account;

    @Column(nullable = false)
    private Instant expiryDate;
} 