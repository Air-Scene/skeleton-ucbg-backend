package com.example.demo.auth.service;

import com.example.demo.auth.dto.AuthResponse;
import com.example.demo.auth.dto.LoginRequest;
import com.example.demo.auth.dto.RegisterRequest;
import com.example.demo.auth.model.RefreshToken;
import com.example.demo.auth.model.UserPrincipal;
import com.example.demo.auth.model.VerificationToken;
import com.example.demo.auth.exception.TokenRefreshException;
import com.example.demo.auth.exception.RegistrationException;
import com.example.demo.auth.repository.VerificationTokenRepository;
import com.example.demo.auth.util.CookieUtil;
import com.example.demo.email.service.EmailService;
import com.example.demo.account.dto.AccountDto;
import com.example.demo.account.model.Account;
import com.example.demo.account.model.AccountRole;
import com.example.demo.account.model.AccountStatus;
import com.example.demo.account.repository.AccountRepository;
import com.example.demo.user.service.UserProfileService;
import com.example.demo.customer.service.CustomerProfileService;
import com.example.demo.admin.service.AdminProfileService;
import com.example.demo.admin.dto.CreateAdminProfileDto;
import com.example.demo.customer.dto.CreateCustomerProfileDto;
import com.example.demo.user.dto.CreateUserProfileDto;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final ModelMapper modelMapper;
    private final CookieUtil cookieUtil;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final EmailService emailService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserProfileService userProfileService;
    private final CustomerProfileService customerProfileService;
    private final AdminProfileService adminProfileService;

    @Transactional
    public AuthResponse authenticateUser(LoginRequest loginRequest, HttpServletResponse response) {
        Authentication authentication = performAuthentication(loginRequest);
        return handleSuccessfulAuthentication(authentication, response);
    }

    @Transactional
    public void register(RegisterRequest registerRequest) {
        validateRegistrationRequest(registerRequest);
    
        Account account = createAccount(registerRequest);
        createInitialProfile(account);
        VerificationToken verificationToken = createVerificationToken(account); 
        sendVerificationEmail(account, verificationToken.getToken());
    }

    @Transactional
    public void verifyEmail(String token) {
        VerificationToken verificationToken = validateAndGetVerificationToken(token);
        updateAccountStatus(verificationToken.getAccount());
        verificationTokenRepository.delete(verificationToken);
    }

    @Transactional
    public AuthResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            String refreshTokenFromCookies = getRefreshTokenFromCookies(request);
            RefreshToken refreshToken = validateRefreshToken(refreshTokenFromCookies);
            return generateNewAccessToken(refreshToken.getAccount(), response);
        } catch (TokenRefreshException e) { 
            cookieUtil.deleteRefreshTokenToCookies(response);
            throw e;
        }
    }

    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = cookieUtil.getRefreshTokenFromCookies(request);

        if (refreshToken != null) {
            refreshTokenService.deleteByRefreshToken(refreshToken);
        }

        cookieUtil.deleteRefreshTokenToCookies(response);
        SecurityContextHolder.clearContext();
    }

    private Authentication performAuthentication(LoginRequest loginRequest) {
        return authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
            )
        );
    }

    private AuthResponse handleSuccessfulAuthentication(Authentication authentication, HttpServletResponse response) {
        String accessToken = jwtService.generateAccessToken(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userPrincipal.getAccount());
        cookieUtil.addRefreshTokenToCookies(response, refreshToken.getToken());

        return createAuthResponse(accessToken, userPrincipal);
    }

    private void validateRegistrationRequest(RegisterRequest registerRequest) {
        if (accountRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RegistrationException("Email already exists");
        }
        if (registerRequest.getRole() == AccountRole.ROLE_ADMIN) {
            throw new RegistrationException("Cannot register with ADMIN role");
        }
    }

    private Account createAccount(RegisterRequest registerRequest) {
        Account account = Account.builder()
            .email(registerRequest.getEmail())
            .passwordHash(passwordEncoder.encode(registerRequest.getPassword()))
            .language(registerRequest.getLanguage())
            .status(AccountStatus.PENDING)
            .role(registerRequest.getRole())
            .build();
        return accountRepository.save(account);
    }

    private VerificationToken createVerificationToken(Account account) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, account);
        return verificationTokenRepository.save(verificationToken);
    }

    private void sendVerificationEmail(Account account, String token) {
        try {
            emailService.sendVerificationEmail(account.getEmail(), token, account.getLanguage());
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    private VerificationToken validateAndGetVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
            .orElseThrow(() -> {
                return new IllegalArgumentException("Invalid verification token");
            });

        if (verificationToken.isExpired()) {
            verificationTokenRepository.delete(verificationToken);
            throw new IllegalArgumentException("Verification token has expired");
        }

        return verificationToken;
    }

    private void updateAccountStatus(Account account) {
        account.setStatus(AccountStatus.ACTIVE);
        accountRepository.save(account);
    }

    private String getRefreshTokenFromCookies(HttpServletRequest request) {
        String refreshToken = cookieUtil.getRefreshTokenFromCookies(request);
        
        if (refreshToken == null) {
            throw new TokenRefreshException("Refresh token not found in cookies");
        }
        return refreshToken;
    }

    private RefreshToken validateRefreshToken(String refreshToken) {
        RefreshToken token = refreshTokenService.findByRefreshToken(refreshToken);
        return refreshTokenService.verifyExpiration(token);
    }

    private AuthResponse generateNewAccessToken(Account account, HttpServletResponse response) {
        UserPrincipal userPrincipal = new UserPrincipal(account);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            userPrincipal,
            null,
            userPrincipal.getAuthorities()
        );
        return createAuthResponse(jwtService.generateAccessToken(authentication), userPrincipal);
    }

    private AuthResponse createAuthResponse(String accessToken, UserPrincipal userPrincipal) {
        return AuthResponse.builder()
            .accessToken(accessToken)
            .account(modelMapper.map(userPrincipal.getAccount(), AccountDto.class))
            .role(userPrincipal.getAccount().getRole())
            .build();
    }

    private void createInitialProfile(Account account) {
        switch (account.getRole()) {
            case ROLE_USER -> {
                CreateUserProfileDto userProfileDto = CreateUserProfileDto.builder()
                    .bio(null)
                    .phoneNumber(null)
                    .address(null)
                    .city(null)
                    .country(null)
                    .postalCode(null)
                    .build();
                userProfileService.create(account.getId(), userProfileDto);
            }
            case ROLE_CUSTOMER -> {
                CreateCustomerProfileDto customerProfileDto = CreateCustomerProfileDto.builder()
                    .companyName(null)
                    .industry(null)
                    .phoneNumber(null)
                    .address(null)
                    .city(null)
                    .country(null)
                    .postalCode(null)
                    .vatNumber(null)
                    .build();
                customerProfileService.create(account.getId(), customerProfileDto);
            }
            case ROLE_ADMIN -> {
                CreateAdminProfileDto adminProfileDto = CreateAdminProfileDto.builder()
                    .department(null)
                    .position(null)
                    .adminLevel(0)
                    .directReportsCount(0)
                    .build();
                adminProfileService.create(account.getId(), adminProfileDto);
            }
        }
    }
}

