// package com.example.demo.auth.service;

// import com.example.demo.auth.model.UserPrincipal;
// import com.example.demo.user.model.User;
// import com.example.demo.user.model.UserRole;
// import com.example.demo.user.model.UserStatus;
// import com.example.demo.user.repository.UserRepository;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;

// import java.util.Optional;
// import java.util.Set;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.Mockito.when;

// @ExtendWith(MockitoExtension.class)
// class CustomUserDetailsServiceTest {

//     @Mock
//     private UserRepository userRepository;

//     @InjectMocks
//     private CustomUserDetailsService userDetailsService;

//     private User testUser;

//     @BeforeEach
//     void setUp() {
//         testUser = User.builder()
//             .email("test@example.com")
//             .passwordHash("hashedPassword")

//             .roles(Set.of(UserRole.ROLE_USER))
//             .build();
//     }

//     @Test
//     void loadUserByUsername_Success() {
//         // Arrange
//         when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

//         // Act
//         UserDetails userDetails = userDetailsService.loadUserByUsername(testUser.getEmail());

//         // Assert
//         assertNotNull(userDetails);
//         assertTrue(userDetails instanceof UserPrincipal);
//         assertEquals(testUser.getEmail(), userDetails.getUsername());
//         assertEquals(testUser.getPasswordHash(), userDetails.getPassword());
//         assertTrue(userDetails.isEnabled());
//         assertFalse(userDetails.getAuthorities().isEmpty());
//     }

//     @Test
//     void loadUserByUsername_UserNotFound() {
//         // Arrange
//         when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

//         // Act & Assert
//         assertThrows(UsernameNotFoundException.class,
//             () -> userDetailsService.loadUserByUsername("nonexistent@example.com"));
//     }

//     @Test
//     void loadUserByUsername_NullEmail() {
//         // Act & Assert
//         assertThrows(UsernameNotFoundException.class,
//             () -> userDetailsService.loadUserByUsername(null));
//     }

//     @Test
//     void loadUserByUsername_EmptyEmail() {
//         // Act & Assert
//         assertThrows(UsernameNotFoundException.class,
//             () -> userDetailsService.loadUserByUsername(""));
//     }

//     @Test
//     void loadUserByUsername_InactiveUser() {
//         // Arrange
//         testUser.setStatus(UserStatus.INACTIVE);
//         when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

//         // Act
//         UserDetails userDetails = userDetailsService.loadUserByUsername(testUser.getEmail());

//         // Assert
//         assertNotNull(userDetails);
//         assertFalse(userDetails.isEnabled());
//     }
// } 