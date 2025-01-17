// package com.example.demo.auth.service;

// import com.example.demo.auth.exception.TokenRefreshException;
// import com.example.demo.auth.model.RefreshToken;
// import com.example.demo.auth.repository.RefreshTokenRepository;
// import com.example.demo.user.model.User;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.test.util.ReflectionTestUtils;

// import java.time.Instant;
// import java.util.Optional;
// import java.util.UUID;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class)
// class RefreshTokenServiceTest {

//     @Mock
//     private RefreshTokenRepository refreshTokenRepository;

//     @InjectMocks
//     private RefreshTokenService refreshTokenService;

//     private User testUser;
//     private RefreshToken testRefreshToken;

//     @BeforeEach
//     void setUp() {
//         ReflectionTestUtils.setField(refreshTokenService, "refreshTokenExpiration", 86400000L); // 24 hours

//         testUser = new User();
//         testUser.setEmail("test@example.com");
//         testUser.setPasswordHash("hashedPassword");

//         testRefreshToken = RefreshToken.builder()
//             .token(UUID.randomUUID().toString())
//             .user(testUser)
//             .expiryDate(Instant.now().plusMillis(86400000L))
//             .build();
//     }

//     @Test
//     void createRefreshToken_Success() {
//         // Arrange
//         when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(testRefreshToken);

//         // Act
//         RefreshToken result = refreshTokenService.createRefreshToken(testUser);

//         // Assert
//         assertNotNull(result);
//         assertEquals(testUser, result.getUser());
//         assertNotNull(result.getToken());
//         assertNotNull(result.getExpiryDate());
//         verify(refreshTokenRepository).deleteByUser(testUser);
//         verify(refreshTokenRepository).save(any(RefreshToken.class));
//     }

//     @Test
//     void findByRefreshToken_Success() {
//         // Arrange
//         when(refreshTokenRepository.findByToken(testRefreshToken.getToken()))
//             .thenReturn(Optional.of(testRefreshToken));

//         // Act
//         RefreshToken result = refreshTokenService.findByRefreshToken(testRefreshToken.getToken());

//         // Assert
//         assertNotNull(result);
//         assertEquals(testRefreshToken.getToken(), result.getToken());
//         assertEquals(testUser, result.getUser());
//     }

//     @Test
//     void findByRefreshToken_TokenNotFound() {
//         // Arrange
//         when(refreshTokenRepository.findByToken(anyString())).thenReturn(Optional.empty());

//         // Act & Assert
//         assertThrows(TokenRefreshException.class,
//             () -> refreshTokenService.findByRefreshToken("non-existent-token"));
//     }

//     @Test
//     void verifyExpiration_ValidToken() {
//         // Arrange
//         RefreshToken validToken = RefreshToken.builder()
//             .token(UUID.randomUUID().toString())
//             .user(testUser)
//             .expiryDate(Instant.now().plusMillis(86400000L))
//             .build();

//         // Act
//         RefreshToken result = refreshTokenService.verifyExpiration(validToken);

//         // Assert
//         assertNotNull(result);
//         assertEquals(validToken, result);
//     }

//     @Test
//     void verifyExpiration_ExpiredToken() {
//         // Arrange
//         RefreshToken expiredToken = RefreshToken.builder()
//             .token(UUID.randomUUID().toString())
//             .user(testUser)
//             .expiryDate(Instant.now().minusMillis(86400000L))
//             .build();

//         // Act & Assert
//         assertThrows(TokenRefreshException.class,
//             () -> refreshTokenService.verifyExpiration(expiredToken));
//         verify(refreshTokenRepository).delete(expiredToken);
//     }

//     @Test
//     void deleteByRefreshToken_Success() {
//         // Arrange
//         when(refreshTokenRepository.findByToken(testRefreshToken.getToken()))
//             .thenReturn(Optional.of(testRefreshToken));

//         // Act
//         refreshTokenService.deleteByRefreshToken(testRefreshToken.getToken());

//         // Assert
//         verify(refreshTokenRepository).delete(testRefreshToken);
//     }

//     @Test
//     void deleteByUser_Success() {
//         // Act
//         refreshTokenService.deleteByUser(testUser);

//         // Assert
//         verify(refreshTokenRepository).deleteByUser(testUser);
//     }

//     @Test
//     void cleanupExpiredTokens_Success() {
//         // Act
//         refreshTokenService.cleanupExpiredTokens();

//         // Assert
//         verify(refreshTokenRepository).deleteByExpiryDateBefore(any(Instant.class));
//     }

//     @Test
//     void revokeAllUserTokens_Success() {
//         // Act
//         refreshTokenService.revokeAllUserTokens(testUser);

//         // Assert
//         verify(refreshTokenRepository).deleteByUser(testUser);
//     }
// } 