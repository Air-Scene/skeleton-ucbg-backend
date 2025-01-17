package com.example.demo.admin.controller;

import com.example.demo.admin.dto.AdminProfileDto;
import com.example.demo.admin.dto.UpdateAdminProfileDto;
import com.example.demo.admin.service.AdminProfileService;
import com.example.demo.auth.model.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admins")
@RequiredArgsConstructor
public class AdminProfileController {
    private final AdminProfileService adminProfileService;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminProfileDto> getOwnProfile(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(adminProfileService.findByAccountId(userPrincipal.getId()));
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminProfileDto> updateOwnProfile(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody UpdateAdminProfileDto updateDto) {
        return ResponseEntity.ok(adminProfileService.update(userPrincipal.getId(), updateDto));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminProfileDto> getProfileById(@PathVariable UUID id) {
        return ResponseEntity.ok(adminProfileService.findById(id));
    }
} 