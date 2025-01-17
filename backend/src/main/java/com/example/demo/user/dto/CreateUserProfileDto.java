package com.example.demo.user.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserProfileDto {
    @Size(max = 500, message = "Bio must not exceed 500 characters")
    private String bio;

    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phoneNumber;

    @Size(max = 100, message = "Address must not exceed 100 characters")
    private String address;

    @Size(max = 50, message = "City must not exceed 50 characters")
    private String city;

    @Size(max = 50, message = "Country must not exceed 50 characters")
    private String country;

    @Size(max = 10, message = "Postal code must not exceed 10 characters")
    private String postalCode;
}