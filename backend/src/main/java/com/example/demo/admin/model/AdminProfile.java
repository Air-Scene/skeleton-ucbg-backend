package com.example.demo.admin.model;

import com.example.demo.account.model.Account;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "admin_profiles", schema = "app")
public class AdminProfile {
    @Id
    @Column(name = "account_id")
    private UUID id;

    @Version
    private Long version;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "account_id", nullable = false, foreignKey = @ForeignKey(name = "fk_admin_profile_account"))
    private Account account;

    @Column(name = "department")
    private String department;

    @Column(name = "position")
    private String position;

    @Column(name = "admin_level")
    private Integer adminLevel;

    @Column(name = "direct_reports_count")
    private Integer directReportsCount;

    @Column(name = "last_access_date")
    private OffsetDateTime lastAccessDate;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }

    public void setAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }
        this.account = account;
        this.id = account.getId();
    }
} 