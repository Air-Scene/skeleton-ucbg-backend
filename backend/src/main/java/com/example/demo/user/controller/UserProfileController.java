package com.example.demo.user.controller;

import com.example.demo.user.dto.UserProfileDto;
import com.example.demo.user.dto.UpdateUserProfileDto;
import com.example.demo.user.service.UserProfileService;
import com.example.demo.auth.model.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService userProfileService;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserProfileDto> getOwnProfile(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(userProfileService.findByAccountId(userPrincipal.getId()));
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserProfileDto> updateOwnProfile(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody UpdateUserProfileDto updateDto) {
        return ResponseEntity.ok(userProfileService.update(userPrincipal.getId(), updateDto));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserProfileDto> getProfileById(@PathVariable UUID id) {
        return ResponseEntity.ok(userProfileService.findById(id));
    }

    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Integer> getUserCount() {
        return ResponseEntity.ok(userProfileService.count());
    }
} 
