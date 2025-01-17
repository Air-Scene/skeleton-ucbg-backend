package com.example.demo.account.model;

public enum AccountRole {
    ROLE_USER,
    ROLE_ADMIN,
    ROLE_CUSTOMER;

    public static void ensureRoleNotNull(AccountRole role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
    }
} 