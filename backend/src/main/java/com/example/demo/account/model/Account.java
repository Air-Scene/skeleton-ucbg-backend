package com.example.demo.account.model;

import com.example.demo.admin.model.AdminProfile;
import com.example.demo.customer.model.CustomerProfile;
import com.example.demo.user.model.UserProfile;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.Collections;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts", schema = "app")
public class Account {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Size(max = 50, message = "First name must not exceed 50 characters")
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50, message = "Last name must not exceed 50 characters")
    @Column(name = "last_name", length = 50)
    private String lastName;

    @NotBlank(message = "Password hash is required")
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @Builder.Default
    private AccountRole role = AccountRole.ROLE_USER;

    @Column(nullable = false)
    @Builder.Default
    private AccountLanguage language = AccountLanguage.de;

    @Column(nullable = false)
    @Builder.Default
    @Enumerated(EnumType.ORDINAL)
    private AccountStatus status = AccountStatus.PENDING;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private AdminProfile adminProfile;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private CustomerProfile customerProfile;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserProfile userProfile;

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

    public boolean hasRole(AccountRole role) {
        AccountRole.ensureRoleNotNull(role);
        return this.role == role;
    }

    public void setRole(AccountRole role) {
        AccountRole.ensureRoleNotNull(role);
        this.role = role;
    }

    public List<SimpleGrantedAuthority> getSpringSecurityAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    public AccountRole getRole() {
        return role;
    }

    public void setAdminProfile(AdminProfile adminProfile) {
        this.adminProfile = adminProfile;
        if (adminProfile != null && adminProfile.getAccount() != this) {
            adminProfile.setAccount(this);
        }
    }

    public void setCustomerProfile(CustomerProfile customerProfile) {
        this.customerProfile = customerProfile;
        if (customerProfile != null && customerProfile.getAccount() != this) {
            customerProfile.setAccount(this);
        }
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
        if (userProfile != null && userProfile.getAccount() != this) {
            userProfile.setAccount(this);
        }
    }
} 