package com.example.demo.user.dto;

import com.example.demo.user.model.UserProfile;
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
public class UserProfileDto {
    private UUID id;
    private String bio;
    private String phoneNumber;
    private String address;
    private String city;
    private String country;
    private String postalCode;
    private OffsetDateTime lastLoginDate;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public static UserProfileDto fromEntity(UserProfile userProfile) {
        if (userProfile == null) {
            return null;
        }
        
        return UserProfileDto.builder()
                .id(userProfile.getId())
                .bio(userProfile.getBio())
                .phoneNumber(userProfile.getPhoneNumber())
                .address(userProfile.getAddress())
                .city(userProfile.getCity())
                .country(userProfile.getCountry())
                .postalCode(userProfile.getPostalCode())
                .lastLoginDate(userProfile.getLastLoginDate())
                .createdAt(userProfile.getCreatedAt())
                .updatedAt(userProfile.getUpdatedAt())
                .build();
    }
} 