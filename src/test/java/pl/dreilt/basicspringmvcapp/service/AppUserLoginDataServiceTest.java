package pl.dreilt.basicspringmvcapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.dreilt.basicspringmvcapp.exception.AppUserNotFoundException;
import pl.dreilt.basicspringmvcapp.repository.AppUserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static pl.dreilt.basicspringmvcapp.service.AppUserLoginDataServiceTestHelper.createUser;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
class AppUserLoginDataServiceTest {
    static final Long userId = 1L;
    static final String userEmail = "user@example.com";
    static final String oldPassword = "user";
    static final String newPassword = "password123";
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AppUserLoginDataService appUserLoginDataService;

    @BeforeEach
    void setUp() {
        Mockito.when(appUserRepository.findByEmail(userEmail)).thenReturn(Optional.of(createUser()));
        Mockito.when(appUserRepository.findById(userId)).thenReturn(Optional.of(createUser()));
    }

    @Test
    void shouldThrowExceptionIfUserIsNotAuthenticated() {
        // then
        assertThatThrownBy(() -> appUserLoginDataService.changePassword(oldPassword, newPassword))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("Can't change password as no Authentication object found in context for current user.");
    }

    @Test
    @WithMockUser(username = "user@example.com", password = "user", roles = {"USER"})
    void shouldChangePasswordIfUserIsAuthenticated() {
        // when
        appUserLoginDataService.changePassword(oldPassword, newPassword);
        // then
        assertThat(authenticationManager).isNotNull();
        assertThat(appUserRepository.findByEmail(userEmail).get().getPassword())
                .isEqualTo(passwordEncoder.encode(newPassword));
    }

    @Test
    @WithMockUser(username = "user@example.com", password = "user", roles = {"USER"})
    void shouldThrowExceptionIfUserDoesNotExistInDatabase() {
        // given
        Mockito.when(appUserRepository.findByEmail(userEmail)).thenReturn(Optional.empty());
        // then
        assertThatThrownBy(() -> appUserLoginDataService.changePassword(oldPassword, newPassword))
                .isInstanceOf(AppUserNotFoundException.class)
                .hasMessage("Current user doesn't exist in database.");
    }

    // ADMIN
    @Test
    @WithMockUser(username = "user@example.com", password = "user", roles = {"USER"})
    void shouldThrowExceptionIfUserHasNotAdminRole() {
        // when
        assertThatThrownBy(() -> appUserLoginDataService.changePassword(userId, newPassword))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("Can't change password as no admin role user found in context for current user.");
    }

    @Test
    @WithMockUser(username = "admin@example.com", password = "admin", roles = {"ADMIN"})
    void shouldChangeUserPassword() {
        // when
        appUserLoginDataService.changePassword(userId, newPassword);
        // then
        assertThat(appUserRepository.findById(userId).get().getPassword())
                .isEqualTo(passwordEncoder.encode(newPassword));
    }

    @Test
    @WithMockUser(username = "admin@example.com", password = "admin", roles = {"ADMIN"})
    void shouldThrowExceptionIfUserIsNotFound() {
        // given
        Mockito.when(appUserRepository.findById(userId)).thenReturn(Optional.empty());
        // when
        assertThatThrownBy(() -> appUserLoginDataService.changePassword(userId, newPassword))
                .isInstanceOf(AppUserNotFoundException.class)
                .hasMessage("User with ID %s not found", userId);
    }
}