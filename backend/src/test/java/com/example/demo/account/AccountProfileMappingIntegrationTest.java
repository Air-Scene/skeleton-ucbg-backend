package com.example.demo.account;

import com.example.demo.account.model.Account;
import com.example.demo.account.model.AccountRole;
import com.example.demo.account.repository.AccountRepository;
import com.example.demo.admin.model.AdminProfile;
import com.example.demo.admin.repository.AdminProfileRepository;
import com.example.demo.customer.model.CustomerProfile;
import com.example.demo.customer.model.CustomerSubscriptionTier;
import com.example.demo.customer.repository.CustomerProfileRepository;
import com.example.demo.user.model.UserProfile;
import com.example.demo.user.repository.UserProfileRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class AccountProfileMappingIntegrationTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AdminProfileRepository adminProfileRepository;

    @Autowired
    private CustomerProfileRepository customerProfileRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    void testAccountAdminProfileMapping() {
        // Create Account first - this is required
        Account account = Account.builder()
                .email("admin@test.com")
                .firstName("Admin")
                .lastName("Test")
                .passwordHash("hash")
                .role(AccountRole.ROLE_ADMIN)
                .build();

        // Create AdminProfile with only profile-specific data
        AdminProfile adminProfile = AdminProfile.builder()
                .department("IT")
                .position("Manager")
                .adminLevel(1)
                .build();

        // Link profile to account
        account.setAdminProfile(adminProfile);

        // Save account which will cascade to profile
        account = accountRepository.save(account);

        // Clear persistence context
        entityManager.flush();
        entityManager.clear();

        // Test Account → AdminProfile
        Account foundAccount = accountRepository.findById(account.getId()).orElseThrow();
        assertThat(foundAccount.getAdminProfile()).isNotNull();
        assertThat(foundAccount.getAdminProfile().getDepartment()).isEqualTo("IT");
        assertThat(foundAccount.getAdminProfile().getAccount()).isSameAs(foundAccount);

        // Test AdminProfile has same ID as Account and proper relationship
        AdminProfile foundProfile = adminProfileRepository.findById(account.getId()).orElseThrow();
        assertThat(foundProfile.getId()).isEqualTo(account.getId());
        assertThat(foundProfile.getDepartment()).isEqualTo("IT");
        assertThat(foundProfile.getAccount().getEmail()).isEqualTo("admin@test.com");
    }

    @Test
    @Transactional
    void testCannotCreateProfileWithoutAccount() {
        // Create AdminProfile
        AdminProfile adminProfile = AdminProfile.builder()
                .department("IT")
                .position("Manager")
                .adminLevel(1)
                .build();

        // Attempting to save directly should fail
        assertThatThrownBy(() -> adminProfileRepository.save(adminProfile))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @Transactional
    void testAccountCustomerProfileMapping() {
        // Create Account first
        Account account = Account.builder()
                .email("customer@test.com")
                .firstName("Customer")
                .lastName("Test")
                .passwordHash("hash")
                .role(AccountRole.ROLE_CUSTOMER)
                .build();

        // Create and set CustomerProfile
        CustomerProfile customerProfile = CustomerProfile.builder()
                .companyName("Test Corp")
                .industry("Technology")
                .subscriptionTier(CustomerSubscriptionTier.PREMIUM)
                .build();

        account.setCustomerProfile(customerProfile);

        // Save account which will cascade to profile
        account = accountRepository.save(account);

        // Clear persistence context
        entityManager.flush();
        entityManager.clear();

        // Test Account → CustomerProfile
        Account foundAccount = accountRepository.findById(account.getId()).orElseThrow();
        assertThat(foundAccount.getCustomerProfile()).isNotNull();
        assertThat(foundAccount.getCustomerProfile().getCompanyName()).isEqualTo("Test Corp");

        // Test CustomerProfile has same ID as Account
        CustomerProfile foundProfile = customerProfileRepository.findById(account.getId()).orElseThrow();
        assertThat(foundProfile.getId()).isEqualTo(account.getId());
        assertThat(foundProfile.getCompanyName()).isEqualTo("Test Corp");
    }

    @Test
    @Transactional
    void testAccountUserProfileMapping() {
        // Create Account first - this is required
        Account account = Account.builder()
                .email("user@test.com")
                .firstName("Regular")
                .lastName("User")
                .passwordHash("hash")
                .role(AccountRole.ROLE_USER)
                .build();

        // Create UserProfile with only profile-specific data
        UserProfile userProfile = UserProfile.builder()
                .bio("Test bio")
                .phoneNumber("123456789")
                .address("Test Street 1")
                .city("Test City")
                .country("Test Country")
                .postalCode("12345")
                .build();

        // Link profile to account
        account.setUserProfile(userProfile);

        // Save account which will cascade to profile
        account = accountRepository.save(account);

        // Clear persistence context
        entityManager.flush();
        entityManager.clear();

        // Test Account → UserProfile
        Account foundAccount = accountRepository.findById(account.getId()).orElseThrow();
        assertThat(foundAccount.getUserProfile()).isNotNull();
        assertThat(foundAccount.getUserProfile().getBio()).isEqualTo("Test bio");
        assertThat(foundAccount.getUserProfile().getAccount()).isSameAs(foundAccount);

        // Test UserProfile has same ID as Account and proper relationship
        UserProfile foundProfile = userProfileRepository.findById(account.getId()).orElseThrow();
        assertThat(foundProfile.getId()).isEqualTo(account.getId());
        assertThat(foundProfile.getBio()).isEqualTo("Test bio");
        assertThat(foundProfile.getAccount().getEmail()).isEqualTo("user@test.com");
    }

    @Test
    @Transactional
    void testCannotCreateUserProfileWithoutAccount() {
        // Create UserProfile
        UserProfile userProfile = UserProfile.builder()
                .bio("Test bio")
                .phoneNumber("123456789")
                .build();

        // Attempting to save directly should fail
        assertThatThrownBy(() -> userProfileRepository.save(userProfile))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @Transactional
    void testCascadeDelete() {
        // Create Account first
        Account account = Account.builder()
                .email("test@test.com")
                .firstName("Test")
                .lastName("User")
                .passwordHash("hash")
                .role(AccountRole.ROLE_ADMIN)
                .build();

        // Create and set profiles
        AdminProfile adminProfile = AdminProfile.builder()
                .department("IT")
                .build();

        CustomerProfile customerProfile = CustomerProfile.builder()
                .companyName("Test Corp")
                .build();

        UserProfile userProfile = UserProfile.builder()
                .bio("Test bio")
                .build();

        account.setAdminProfile(adminProfile);
        account.setCustomerProfile(customerProfile);
        account.setUserProfile(userProfile);

        // Save account which will cascade to profiles
        account = accountRepository.save(account);

        // Clear persistence context
        entityManager.flush();
        entityManager.clear();

        // Delete Account
        accountRepository.deleteById(account.getId());

        // Verify cascaded deletes
        assertThat(adminProfileRepository.existsById(account.getId())).isFalse();
        assertThat(customerProfileRepository.existsById(account.getId())).isFalse();
        assertThat(userProfileRepository.existsById(account.getId())).isFalse();
    }

    @Test
    @Transactional
    void testLazyLoading() {
        // Create Account first
        Account account = Account.builder()
                .email("lazy@test.com")
                .firstName("Lazy")
                .lastName("Test")
                .passwordHash("hash")
                .role(AccountRole.ROLE_USER)
                .build();

        // Create and set UserProfile
        UserProfile userProfile = UserProfile.builder()
                .bio("Test bio")
                .build();

        account.setUserProfile(userProfile);

        // Save account which will cascade to profile
        account = accountRepository.save(account);

        // Clear persistence context
        entityManager.flush();
        entityManager.clear();

        // Load Account without accessing profile
        Account foundAccount = accountRepository.findById(account.getId()).orElseThrow();
        
        // Verify profile hasn't been loaded yet
        assertThat(entityManager.getEntityManagerFactory()
                .getPersistenceUnitUtil().isLoaded(foundAccount, "userProfile"))
                .isFalse();

        // Access profile to trigger loading
        UserProfile profile = foundAccount.getUserProfile();
        assertThat(profile.getBio()).isEqualTo("Test bio");

        // Verify profile is now loaded
        assertThat(entityManager.getEntityManagerFactory()
                .getPersistenceUnitUtil().isLoaded(foundAccount, "userProfile"))
                .isTrue();
    }

    @Test
    @Transactional
    void testAdminProfileLazyLoading() {
        // Create Account first
        Account account = Account.builder()
                .email("admin-lazy@test.com")
                .firstName("Admin")
                .lastName("Test")
                .passwordHash("hash")
                .role(AccountRole.ROLE_ADMIN)
                .build();

        // Create and set AdminProfile
        AdminProfile adminProfile = AdminProfile.builder()
                .department("IT")
                .build();

        account.setAdminProfile(adminProfile);

        // Save account which will cascade to profile
        account = accountRepository.save(account);

        // Clear persistence context
        entityManager.flush();
        entityManager.clear();

        // Load Account without accessing profile
        Account foundAccount = accountRepository.findById(account.getId()).orElseThrow();
        
        // Verify profile hasn't been loaded yet
        assertThat(entityManager.getEntityManagerFactory()
                .getPersistenceUnitUtil().isLoaded(foundAccount, "adminProfile"))
                .isFalse();

        // Access profile to trigger loading
        AdminProfile profile = foundAccount.getAdminProfile();
        assertThat(profile.getDepartment()).isEqualTo("IT");

        // Verify profile is now loaded
        assertThat(entityManager.getEntityManagerFactory()
                .getPersistenceUnitUtil().isLoaded(foundAccount, "adminProfile"))
                .isTrue();
    }

    @Test
    @Transactional
    void testCustomerProfileLazyLoading() {
        // Create Account first
        Account account = Account.builder()
                .email("customer-lazy@test.com")
                .firstName("Customer")
                .lastName("Test")
                .passwordHash("hash")
                .role(AccountRole.ROLE_CUSTOMER)
                .build();

        // Create and set CustomerProfile
        CustomerProfile customerProfile = CustomerProfile.builder()
                .companyName("Test Corp")
                .build();

        account.setCustomerProfile(customerProfile);

        // Save account which will cascade to profile
        account = accountRepository.save(account);

        // Clear persistence context
        entityManager.flush();
        entityManager.clear();

        // Load Account without accessing profile
        Account foundAccount = accountRepository.findById(account.getId()).orElseThrow();
        
        // Verify profile hasn't been loaded yet
        assertThat(entityManager.getEntityManagerFactory()
                .getPersistenceUnitUtil().isLoaded(foundAccount, "customerProfile"))
                .isFalse();

        // Access profile to trigger loading
        CustomerProfile profile = foundAccount.getCustomerProfile();
        assertThat(profile.getCompanyName()).isEqualTo("Test Corp");

        // Verify profile is now loaded
        assertThat(entityManager.getEntityManagerFactory()
                .getPersistenceUnitUtil().isLoaded(foundAccount, "customerProfile"))
                .isTrue();
    }

    @Test
    @Transactional
    void testUserProfileLazyLoading() {
        // Create Account first
        Account account = Account.builder()
                .email("user-lazy@test.com")
                .firstName("User")
                .lastName("Test")
                .passwordHash("hash")
                .role(AccountRole.ROLE_USER)
                .build();

        // Create and set UserProfile
        UserProfile userProfile = UserProfile.builder()
                .bio("Test bio")
                .build();

        account.setUserProfile(userProfile);

        // Save account which will cascade to profile
        account = accountRepository.save(account);

        // Clear persistence context
        entityManager.flush();
        entityManager.clear();

        // Load Account without accessing profile
        Account foundAccount = accountRepository.findById(account.getId()).orElseThrow();
        
        // Verify profile hasn't been loaded yet
        assertThat(entityManager.getEntityManagerFactory()
                .getPersistenceUnitUtil().isLoaded(foundAccount, "userProfile"))
                .isFalse();

        // Access profile to trigger loading
        UserProfile profile = foundAccount.getUserProfile();
        assertThat(profile.getBio()).isEqualTo("Test bio");

        // Verify profile is now loaded
        assertThat(entityManager.getEntityManagerFactory()
                .getPersistenceUnitUtil().isLoaded(foundAccount, "userProfile"))
                .isTrue();
    }
} 