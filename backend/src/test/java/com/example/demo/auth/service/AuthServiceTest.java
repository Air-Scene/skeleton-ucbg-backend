// package com.example.demo.auth.service;

// import com.example.demo.auth.dto.AuthResponse;
// import com.example.demo.auth.dto.LoginRequest;
// import com.example.demo.auth.model.RefreshToken;
// import com.example.demo.auth.model.UserPrincipal;
// import com.example.demo.auth.util.CookieUtil;
// import com.example.demo.user.dto.UserDto;
// import com.example.demo.user.model.User;
// import com.example.demo.user.model.UserStatus;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.modelmapper.ModelMapper;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;

// import java.util.Collections;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.*;
// import static org.junit.jupiter.api.Assertions.*;

// @ExtendWith(MockitoExtension.class)
// class AuthServiceTest {

//     @Mock
//     private AuthenticationManager authenticationManager;
//     @Mock
//     private JwtService jwtService;
//     @Mock
//     private RefreshTokenService refreshTokenService;
//     @Mock
//     private ModelMapper modelMapper;
//     @Mock
//     private CookieUtil cookieUtil;
//     @Mock
//     private HttpServletResponse response;
//     @Mock
//     private HttpServletRequest request;

//     @InjectMocks
//     private AuthService authService;

//     private User testUser;
//     private UserPrincipal userPrincipal;
//     private LoginRequest loginRequest;
//     private Authentication authentication;
//     private RefreshToken refreshToken;
//     private UserDto userDto;

//     @BeforeEach
//     void setUp() {
//         testUser = new User();
//         testUser.setEmail("test@example.com");
//         testUser.setPasswordHash("hashedPassword");
//         testUser.setStatus(UserStatus.ACTIVE);

//         userPrincipal = new UserPrincipal(testUser);
        
//         loginRequest = new LoginRequest();
//         loginRequest.setEmail("test@example.com");
//         loginRequest.setPassword("password123");

//         authentication = new UsernamePasswordAuthenticationToken(
//             userPrincipal,
//             null,
//             Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
//         );

//         refreshToken = RefreshToken.builder()
//             .token("refresh-token-123")
//             .user(testUser)
//             .build();

//         userDto = new UserDto();
//         userDto.setEmail(testUser.getEmail());
//     }

//     @Test
//     void authenticateUser_Success() {
//         // Arrange
//         when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
//         when(jwtService.generateAccessToken(authentication)).thenReturn("access-token-123");
//         when(refreshTokenService.createRefreshToken(testUser)).thenReturn(refreshToken);
//         when(modelMapper.map(testUser, UserDto.class)).thenReturn(userDto);

//         // Act
//         AuthResponse response = authService.authenticateUser(loginRequest, this.response);

//         // Assert
//         assertNotNull(response);
//         assertEquals("access-token-123", response.getAccessToken());
//         assertEquals("Bearer", response.getTokenType());
//         assertEquals(testUser.getEmail(), response.getEmail());
//         verify(refreshTokenService).revokeAllUserTokens(testUser);
//         verify(cookieUtil).addRefreshTokenToCookies(this.response, refreshToken.getToken());
//     }

//     @Test
//     void refreshToken_Success() {
//         // Arrange
//         when(cookieUtil.getRefreshTokenFromCookies(request)).thenReturn("refresh-token-123");
//         when(refreshTokenService.findByRefreshToken("refresh-token-123")).thenReturn(refreshToken);
//         when(refreshTokenService.verifyExpiration(refreshToken)).thenReturn(refreshToken);
//         when(jwtService.generateAccessToken(any(Authentication.class))).thenReturn("new-access-token-123");
//         when(modelMapper.map(testUser, UserDto.class)).thenReturn(userDto);

//         // Act
//         AuthResponse response = authService.refreshToken(request, this.response);

//         // Assert
//         assertNotNull(response);
//         assertEquals("new-access-token-123", response.getAccessToken());
//         assertEquals("Bearer", response.getTokenType());
//         assertEquals(testUser.getEmail(), response.getEmail());
//     }

//     @Test
//     void logout_Success() {
//         // Arrange
//         when(cookieUtil.getRefreshTokenFromCookies(request)).thenReturn("refresh-token-123");

//         // Act
//         authService.logout(request, response);

//         // Assert
//         verify(refreshTokenService).deleteByRefreshToken("refresh-token-123");
//         verify(cookieUtil).deleteRefreshTokenToCookies(response);
//     }

//     @Test
//     void logout_WithNullRefreshToken_Success() {
//         // Arrange
//         when(cookieUtil.getRefreshTokenFromCookies(request)).thenReturn(null);

//         // Act
//         authService.logout(request, response);

//         // Assert
//         verify(refreshTokenService, never()).deleteByRefreshToken(any());
//         verify(cookieUtil).deleteRefreshTokenToCookies(response);
//     }
// } 