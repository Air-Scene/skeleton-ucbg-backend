package com.example.demo.customer.controller;

import com.example.demo.customer.dto.CustomerProfileDto;
import com.example.demo.customer.dto.UpdateCustomerProfileDto;
import com.example.demo.customer.service.CustomerProfileService;
import com.example.demo.auth.model.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerProfileController {
    private final CustomerProfileService customerProfileService;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CustomerProfileDto> getOwnProfile(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(customerProfileService.findByAccountId(userPrincipal.getId()));
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CustomerProfileDto> updateOwnProfile(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody UpdateCustomerProfileDto updateDto) {
        return ResponseEntity.ok(customerProfileService.update(userPrincipal.getId(), updateDto));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerProfileDto> getProfileById(@PathVariable UUID id) {
        return ResponseEntity.ok(customerProfileService.findById(id));
    }

    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Integer> getCustomerCount() {
        return ResponseEntity.ok(customerProfileService.count());
    }
} 