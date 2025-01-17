// package com.example.demo.auth.service;

// import com.example.demo.auth.model.UserPrincipal;
// import com.example.demo.user.model.User;
// import io.jsonwebtoken.ExpiredJwtException;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.test.util.ReflectionTestUtils;

// import java.util.Collections;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.Mockito.when;

// @ExtendWith(MockitoExtension.class)
// class JwtServiceTest {

//     @Mock
//     private CustomUserDetailsService userDetailsService;

//     @InjectMocks
//     private JwtService jwtService;

//     private User testUser;
//     private UserPrincipal userPrincipal;
//     private Authentication authentication;

//     @BeforeEach
//     void setUp() {
//         // Set up JWT properties
//         ReflectionTestUtils.setField(jwtService, "jwtSecret", "testSecretKeyWithAtLeast256BitsForHS512SignatureAlgorithm");
//         ReflectionTestUtils.setField(jwtService, "jwtExpiration", 3600000); // 1 hour
//         jwtService.init(); // Initialize the key

//         // Set up test user and authentication
//         testUser = new User();
//         testUser.setEmail("test@example.com");
//         testUser.setPasswordHash("hashedPassword");

//         userPrincipal = new UserPrincipal(testUser);
        
//         authentication = new UsernamePasswordAuthenticationToken(
//             userPrincipal,
//             null,
//             Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
//         );

//         when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userPrincipal);
//     }

//     @Test
//     void generateAccessToken_Success() {
//         // Act
//         String token = jwtService.generateAccessToken(authentication);

//         // Assert
//         assertNotNull(token);
//         assertTrue(jwtService.validateAccessToken(token));
//         assertEquals("test@example.com", jwtService.getUsernameFromAccessToken(token));   
//     }

//     @Test
//     void validateAccessToken_ValidToken_ReturnsTrue() {
//         // Arrange
//         String token = jwtService.generateAccessToken(authentication);

//         // Act & Assert
//         assertTrue(jwtService.validateAccessToken(token));
//     }

//     @Test
//     void validateAccessToken_InvalidToken_ReturnsFalse() {
//         // Act & Assert
//         assertFalse(jwtService.validateAccessToken("invalid.token.here"));
//     }

//     @Test
//     void validateAccessToken_ExpiredToken_ThrowsException() {
//         // Arrange
//         ReflectionTestUtils.setField(jwtService, "jwtExpiration", -3600000); // -1 hour
//         String token = jwtService.generateAccessToken(authentication);

//         // Act & Assert
//         assertThrows(ExpiredJwtException.class, () -> jwtService.validateAccessToken(token));
//     }

//     @Test
//     void getAuthentication_ValidToken_ReturnsAuthentication() {
//         // Arrange
//         String token = jwtService.generateAccessToken(authentication);

//         // Act
//         Authentication resultAuth = jwtService.getAuthenticationFromAccessToken(token);

//         // Assert
//         assertNotNull(resultAuth);
//         assertEquals(userPrincipal.getUsername(), ((UserPrincipal) resultAuth.getPrincipal()).getUsername());
//     }

//     @Test
//     void getUsernameFromAccessToken_ValidToken_ReturnsUsername() {
//         // Arrange
//         String token = jwtService.generateAccessToken(authentication);

//         // Act
//         String username = jwtService.getUsernameFromAccessToken(token);

//         // Assert
//         assertEquals("test@example.com", username);
//     }
// } 