package com.example.demo.config;

import com.example.demo.account.model.Account;
import com.example.demo.account.model.AccountRole;
import com.example.demo.account.model.AccountLanguage;
import com.example.demo.account.model.AccountStatus;
import com.example.demo.account.repository.AccountRepository;
import com.example.demo.admin.dto.CreateAdminProfileDto;
import com.example.demo.admin.service.AdminProfileService;
import com.example.demo.user.dto.CreateUserProfileDto;
import com.example.demo.user.service.UserProfileService;
import com.example.demo.customer.dto.CreateCustomerProfileDto;
import com.example.demo.customer.service.CustomerProfileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Backoff;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseInitializer {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminProfileService adminProfileService;
    private final UserProfileService userProfileService;
    private final CustomerProfileService customerProfileService;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void init() {
        try {
            createAdminAccount();
            createUserAccount();
            createCustomerAccount();
            log.info("Database initialization completed successfully");
        } catch (Exception e) {
            log.error("Error during database initialization", e);
        }
    }

    private void createAdminAccount() {
        String adminEmail = "admin@example.com";
        if (!accountRepository.existsByEmail(adminEmail)) {
            Account adminUser = Account.builder()
                    .email(adminEmail)
                    .passwordHash(passwordEncoder.encode("password123"))
                    .firstName("Admin")
                    .lastName("Admin")
                    .role(AccountRole.ROLE_ADMIN)
                    .language(AccountLanguage.en)
                    .status(AccountStatus.ACTIVE)
                    .build();
            adminUser = accountRepository.saveAndFlush(adminUser);

            CreateAdminProfileDto adminProfileDto = CreateAdminProfileDto.builder()
                    .department("IT")
                    .position("System Administrator")
                    .adminLevel(1)
                    .directReportsCount(0)
                    .build();
            adminProfileService.create(adminUser.getId(), adminProfileDto);
        }
    }

    private void createUserAccount() {
        String userEmail = "user@example.com";
        if (!accountRepository.existsByEmail(userEmail)) {
            Account user = Account.builder()
                    .email(userEmail)
                    .passwordHash(passwordEncoder.encode("password123"))
                    .firstName("User")
                    .lastName("User")
                    .role(AccountRole.ROLE_USER)
                    .language(AccountLanguage.de)
                    .status(AccountStatus.ACTIVE)
                    .build();
            user = accountRepository.saveAndFlush(user);

            CreateUserProfileDto userProfileDto = CreateUserProfileDto.builder()
                    .bio("Regular user account")
                    .phoneNumber("+49123456789")
                    .address("Sample Street 123")
                    .city("Berlin")
                    .country("Germany")
                    .postalCode("12345")
                    .build();
            userProfileService.create(user.getId(), userProfileDto);
        }
    }

    private void createCustomerAccount() {
        String customerEmail = "customer@example.com";
        if (!accountRepository.existsByEmail(customerEmail)) {
            Account customerUser = Account.builder()
                    .email(customerEmail)
                    .passwordHash(passwordEncoder.encode("password123"))
                    .firstName("Customer")
                    .lastName("Customer")
                    .role(AccountRole.ROLE_CUSTOMER)
                    .language(AccountLanguage.tr)
                    .status(AccountStatus.ACTIVE)
                    .build();
            customerUser = accountRepository.saveAndFlush(customerUser);

            CreateCustomerProfileDto customerProfileDto = CreateCustomerProfileDto.builder()
                    .companyName("Sample Company")
                    .industry("Technology")
                    .phoneNumber("+90123456789")
                    .address("Business Street 456")
                    .city("Istanbul")
                    .country("Turkey")
                    .postalCode("34000")
                    .vatNumber("TR1234567890")
                    .build();
            customerProfileService.create(customerUser.getId(), customerProfileDto);
        }
    }
} 