// package com.example.demo.auth.controller;

// import com.example.demo.auth.dto.AuthResponse;
// import com.example.demo.auth.dto.LoginRequest;
// import com.example.demo.auth.service.AuthService;
// import com.example.demo.auth.service.PasswordResetService;
// import com.example.demo.account.dto.AccountDto;
// import com.example.demo.account.model.AccountStatus;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.http.MediaType;
// import org.springframework.security.authentication.BadCredentialsException;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// import jakarta.servlet.http.HttpServletResponse;

// import java.util.Set;
// import java.util.UUID;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @ExtendWith(MockitoExtension.class)
// class AuthControllerTest {

//     private MockMvc mockMvc;

//     @Mock
//     private AuthService authService;

//     @Mock
//     private PasswordResetService passwordResetService;

//     private ObjectMapper objectMapper;
//     private LoginRequest loginRequest;
//     private AuthResponse authResponse;

//     @BeforeEach
//     void setUp() {
//         objectMapper = new ObjectMapper();
//         mockMvc = MockMvcBuilders
//             .standaloneSetup(new AuthController(authService, passwordResetService))
//             .build();

//         // Setup test data
//         loginRequest = new LoginRequest();
//         loginRequest.setEmail("test@example.com");
//         loginRequest.setPassword("password123");

//         // Create test UserDto
//         AccountDto accountDto = new AccountDto();
//         accountDto.setId(UUID.randomUUID());
//         accountDto.setEmail("test@example.com");
//         accountDto.setFirstName("Test");
//         accountDto.setLastName("User");
//         accountDto.setStatus(AccountStatus.ACTIVE);

//         authResponse = new AuthResponse(
//             "test.jwt.token",
//             "Bearer",
//             "test@example.com",
//             "ROLE_USER",
//             accountDto
//         );
//     }

//     @Test
//     void testLoginSuccess() throws Exception {
//         // Given
//         when(authService.authenticateUser(any(LoginRequest.class), any(HttpServletResponse.class)))
//             .thenReturn(authResponse);

//         // When/Then
//         mockMvc.perform(post("/api/auth/login")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(loginRequest)))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.token").value("test.jwt.token"))
//                 .andExpect(jsonPath("$.refreshToken").value("refresh.token"))
//                 .andExpect(jsonPath("$.tokenType").value("Bearer"))
//                 .andExpect(jsonPath("$.email").value("test@example.com"))
//                 .andExpect(jsonPath("$.user.email").value("test@example.com"));
//     }

//     @Test
//     void testLoginFailure_InvalidCredentials() throws Exception {
//         // Given
//         when(authService.authenticateUser(any(LoginRequest.class), any(HttpServletResponse.class)))
//             .thenThrow(new BadCredentialsException("Invalid credentials"));

//         // When/Then
//         mockMvc.perform(post("/api/auth/login")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(loginRequest)))
//                 .andExpect(status().isUnauthorized())
//                 .andExpect(jsonPath("$.message").value("Invalid credentials"));
//     }

//     @Test
//     void testLoginFailure_NonexistentUser() throws Exception {
//         // Given
//         loginRequest.setEmail("nonexistent@example.com");
//         when(authService.authenticateUser(any(LoginRequest.class), any(HttpServletResponse.class)))
//             .thenThrow(new UsernameNotFoundException("User not found"));

//         // When/Then
//         mockMvc.perform(post("/api/auth/login")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(loginRequest)))
//                 .andExpect(status().isUnauthorized())
//                 .andExpect(jsonPath("$.message").value("User not found"));
//     }
// } 