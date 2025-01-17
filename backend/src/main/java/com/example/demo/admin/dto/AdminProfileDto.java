package com.example.demo.admin.dto;

import com.example.demo.admin.model.AdminProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminProfileDto {
    private UUID id;
    private String department;
    private String position;
    private Integer adminLevel;
    private Integer directReportsCount;
    private OffsetDateTime lastAccessDate;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public static AdminProfileDto fromEntity(AdminProfile adminProfile) {
        if (adminProfile == null) {
            return null;
        }
        
        return AdminProfileDto.builder()
                .id(adminProfile.getId())
                .department(adminProfile.getDepartment())
                .position(adminProfile.getPosition())
                .adminLevel(adminProfile.getAdminLevel())
                .directReportsCount(adminProfile.getDirectReportsCount())
                .lastAccessDate(adminProfile.getLastAccessDate())
                .createdAt(adminProfile.getCreatedAt())
                .updatedAt(adminProfile.getUpdatedAt())
                .build();
    }
} 