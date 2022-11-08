package pl.dreilt.basicspringmvcapp.service;

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
import pl.dreilt.basicspringmvcapp.entity.AppUser;
import pl.dreilt.basicspringmvcapp.entity.AppUserRole;
import pl.dreilt.basicspringmvcapp.exception.AppUserNotFoundException;
import pl.dreilt.basicspringmvcapp.repository.AppUserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
class AppUserLoginDataServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AppUserLoginDataService appUserLoginDataService;

    @Test
    void shouldThrowExceptionIfUserIsNotAuthenticated() {
        // then
        assertThatThrownBy(() -> appUserLoginDataService.changePassword("user", "password123"))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("Can't change password as no Authentication object found in context for current user.");
    }

    @Test
    @WithMockUser(username = "user@example.com", password = "user", roles = { "USER" })
    void shouldChangePasswordIfUserIsAuthenticated() {
        // given
        Set<AppUserRole> userRole = new HashSet<>();
        userRole.add(new AppUserRole(1L, "USER", "Użytkownik", "Dostęp ograniczony"));
        AppUser user = new AppUser(1L, "User", "User", "user@example.com", "{bcrypt}$2a$10$HdTWfiLZkzlT9JXh/q3OhuPMsp972q0rp9oHba0wgaI0P.zRsLfb6", "", "", true, true, userRole);
        Mockito.when(appUserRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        // when
        appUserLoginDataService.changePassword("user", "password123");
        // then
        assertThat(authenticationManager).isNotNull();
        assertThat(appUserRepository.findByEmail("user@example.com").get().getPassword())
                .isEqualTo(passwordEncoder.encode("password123"));
    }

    @Test
    @WithMockUser(username = "user@example.com", password = "user", roles = { "USER" })
    void shouldThrowExceptionIfUserDoesNotExistInDatabase() {
        // given
        Mockito.when(appUserRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());
        // then
        assertThatThrownBy(() -> appUserLoginDataService.changePassword("user", "password123"))
                .isInstanceOf(AppUserNotFoundException.class)
                .hasMessage("Current user doesn't exist in database.");
    }

    // ADMIN
    @Test
    @WithMockUser(username = "user@example.com", password = "user", roles = { "USER" })
    void shouldThrowExceptionIfUserHasNotAdminRole() {
        // given
        Set<AppUserRole> userRole = new HashSet<>();
        userRole.add(new AppUserRole(1L, "USER", "Użytkownik", "Dostęp ograniczony"));
        AppUser user = new AppUser(1L, "Jan", "Kowalski", "jankowalski@example.com", "{bcrypt}$2a$10$HdTWfiLZkzlT9JXh/q3OhuPMsp972q0rp9oHba0wgaI0P.zRsLfb6", "", "", true, true, userRole);
        Mockito.when(appUserRepository.findById(1L)).thenReturn(Optional.of(user));
        // when
        assertThatThrownBy(() -> appUserLoginDataService.changePassword(1L, "password123"))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("Can't change password as no admin role user found in context for current user.");
    }

    @Test
    @WithMockUser(username = "admin@example.com", password = "admin", roles = { "ADMIN" })
    void shouldChangeUserPassword() {
        // given
        Long userId = 1L;
        String newPassword = "password123";
        Set<AppUserRole> userRole = new HashSet<>();
        userRole.add(new AppUserRole(userId, "USER", "Użytkownik", "Dostęp ograniczony"));
        AppUser user = new AppUser(userId, "Jan", "Kowalski", "jankowalski@example.com", "{bcrypt}$2a$10$HdTWfiLZkzlT9JXh/q3OhuPMsp972q0rp9oHba0wgaI0P.zRsLfb6", "", "", true, true, userRole);
        Mockito.when(appUserRepository.findById(userId)).thenReturn(Optional.of(user));
        // when
        appUserLoginDataService.changePassword(userId, newPassword);
        // then
        assertThat(appUserRepository.findById(userId).get().getPassword())
                .isEqualTo(passwordEncoder.encode(newPassword));
    }

    @Test
    @WithMockUser(username = "admin@example.com", password = "admin", roles = { "ADMIN" })
    void shouldThrowExceptionIfUserIsNotFound() {
        // given
        Long userId = 1L;
        String newPassword = "password123";
        Mockito.when(appUserRepository.findById(userId)).thenReturn(Optional.empty());
        // when
        assertThatThrownBy(() -> appUserLoginDataService.changePassword(userId, newPassword))
                .isInstanceOf(AppUserNotFoundException.class)
                .hasMessage("User with ID %s not found", userId);
    }
}