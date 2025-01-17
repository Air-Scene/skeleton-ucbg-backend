package com.example.demo.customer.dto;

import com.example.demo.customer.model.CustomerProfile;
import com.example.demo.customer.model.CustomerSubscriptionTier;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProfileDto {
    private UUID id;
    private String companyName;
    private String industry;
    private String phoneNumber;
    private String address;
    private String city;
    private String country;
    private String postalCode;
    private String vatNumber;
    private CustomerSubscriptionTier subscriptionTier;
    private OffsetDateTime subscriptionStartDate;
    private OffsetDateTime subscriptionEndDate;
    private OffsetDateTime lastPurchaseDate;
    private Integer totalPurchases;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public static CustomerProfileDto fromEntity(CustomerProfile customerProfile) {
        if (customerProfile == null) {
            return null;
        }
        
        return CustomerProfileDto.builder()
                .id(customerProfile.getId())
                .companyName(customerProfile.getCompanyName())
                .industry(customerProfile.getIndustry())
                .phoneNumber(customerProfile.getPhoneNumber())
                .address(customerProfile.getAddress())
                .city(customerProfile.getCity())
                .country(customerProfile.getCountry())
                .postalCode(customerProfile.getPostalCode())
                .vatNumber(customerProfile.getVatNumber())
                .subscriptionTier(customerProfile.getSubscriptionTier())
                .subscriptionStartDate(customerProfile.getSubscriptionStartDate())
                .subscriptionEndDate(customerProfile.getSubscriptionEndDate())
                .lastPurchaseDate(customerProfile.getLastPurchaseDate())
                .totalPurchases(customerProfile.getTotalPurchases())
                .createdAt(customerProfile.getCreatedAt())
                .updatedAt(customerProfile.getUpdatedAt())
                .build();
    }
} 