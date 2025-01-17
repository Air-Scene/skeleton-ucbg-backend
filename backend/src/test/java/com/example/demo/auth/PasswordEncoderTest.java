package com.example.demo.auth;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordEncoderTest {

    @Test
    public void testPasswordEncoding() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "password123";
        
        // Generate a new hash and print it
        String newHash = encoder.encode(rawPassword);
        System.out.println("New hash for 'password123': " + newHash);
        
        // Verify that we can match the password against the new hash
        assertTrue(encoder.matches(rawPassword, newHash), "Password should match the new hash");
        
        // Verify against the hash stored in V2__add_test_user.sql
        String storedHash = "$2a$10$/gxAoEsgt32asUKz9kar9uBmc2d/8yK6QhpDLdg.OGg7rbcpFXQim";
        assertTrue(encoder.matches(rawPassword, storedHash), "Password should match the stored hash");
    }
}