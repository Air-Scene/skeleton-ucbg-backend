package com.example.demo.customer.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerProfileDto {
    @NotNull(message = "Company name is required")
    @Size(max = 100, message = "Company name must not exceed 100 characters")
    private String companyName;

    @NotNull(message = "Industry is required")
    @Size(max = 50, message = "Industry must not exceed 50 characters")
    private String industry;

    @NotNull(message = "Phone number is required")
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phoneNumber;

    @NotNull(message = "Address is required")
    @Size(max = 100, message = "Address must not exceed 100 characters")
    private String address;

    @NotNull(message = "City is required")
    @Size(max = 50, message = "City must not exceed 50 characters")
    private String city;

    @NotNull(message = "Country is required")
    @Size(max = 50, message = "Country must not exceed 50 characters")
    private String country;

    @NotNull(message = "Postal code is required")
    @Size(max = 10, message = "Postal code must not exceed 10 characters")
    private String postalCode;

    @NotNull(message = "VAT number is required")
    @Size(max = 20, message = "VAT number must not exceed 20 characters")
    private String vatNumber;
} 