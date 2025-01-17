package com.example.demo.customer.model;

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
@Table(name = "customer_profiles", schema = "app")
public class CustomerProfile {
    @Id
    @Column(name = "account_id")
    private UUID id;

    @Version
    private Long version;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "account_id", nullable = false, foreignKey = @ForeignKey(name = "fk_customer_profile_account"))
    private Account account;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "industry")
    private String industry;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "vat_number")
    private String vatNumber;

    @Column(name = "subscription_tier")
    @Enumerated(EnumType.STRING)
    private CustomerSubscriptionTier subscriptionTier;

    @Column(name = "subscription_start_date")
    private OffsetDateTime subscriptionStartDate;

    @Column(name = "subscription_end_date")
    private OffsetDateTime subscriptionEndDate;

    @Column(name = "last_purchase_date")
    private OffsetDateTime lastPurchaseDate;

    @Column(name = "total_purchases")
    private Integer totalPurchases;

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

    @Builder(builderMethodName = "internalBuilder")
    protected static CustomerProfile createProfile(
            String companyName,
            String industry,
            String phoneNumber,
            String address,
            String city,
            String country,
            String postalCode,
            String vatNumber,
            CustomerSubscriptionTier subscriptionTier,
            OffsetDateTime subscriptionStartDate,
            OffsetDateTime subscriptionEndDate,
            OffsetDateTime lastPurchaseDate,
            Integer totalPurchases) {
        CustomerProfile profile = new CustomerProfile();
        profile.companyName = companyName;
        profile.industry = industry;
        profile.phoneNumber = phoneNumber;
        profile.address = address;
        profile.city = city;
        profile.country = country;
        profile.postalCode = postalCode;
        profile.vatNumber = vatNumber;
        profile.subscriptionTier = subscriptionTier;
        profile.subscriptionStartDate = subscriptionStartDate;
        profile.subscriptionEndDate = subscriptionEndDate;
        profile.lastPurchaseDate = lastPurchaseDate;
        profile.totalPurchases = totalPurchases;
        return profile;
    }

    public static CustomerProfileBuilder builder() {
        return internalBuilder();
    }
} 