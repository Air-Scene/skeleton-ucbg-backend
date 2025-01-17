package com.example.demo.admin.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAdminProfileDto {
    @Size(max = 100, message = "Department must not exceed 100 characters")
    private String department;

    @Size(max = 100, message = "Position must not exceed 100 characters")
    private String position;

    @Min(value = 0, message = "Admin level must be at least 0")
    @Max(value = 10, message = "Admin level must not exceed 10")
    private Integer adminLevel;

    @Min(value = 0, message = "Direct reports count must be at least 0")
    private Integer directReportsCount;
} 