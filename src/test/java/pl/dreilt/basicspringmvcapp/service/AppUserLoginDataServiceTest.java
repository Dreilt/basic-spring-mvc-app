package pl.dreilt.basicspringmvcapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.dreilt.basicspringmvcapp.config.AppUserDetails;
import pl.dreilt.basicspringmvcapp.entity.AppUser;
import pl.dreilt.basicspringmvcapp.exception.AppUserNotFoundException;
import pl.dreilt.basicspringmvcapp.repository.AppUserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static pl.dreilt.basicspringmvcapp.core.AppUserHelper.createAppUser;
import static pl.dreilt.basicspringmvcapp.service.AppUserServiceTestHelper.createAppUserDetails;

@ExtendWith(MockitoExtension.class)
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
        // given
        SecurityContextHolder.getContext().setAuthentication(null);
        // then
        assertThatThrownBy(() -> appUserLoginDataService.changePassword("user1", "qwerty"))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("Can't change password as no Authentication object found in context for current user.");
    }

    @Test
    void shouldChangePasswordIfUserIsAuthenticated() {
        // given
        AppUserDetails userDetails = createAppUserDetails("Jan", "Kowalski", "jankowalski@example.com", "USER");
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, "user1", userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        AppUser user = createAppUser(2L, "Jan", "Kowalski", "jankowalski@example.com");
        when(appUserRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        // when
        appUserLoginDataService.changePassword("user1", "qwerty");
        // then
        assertThat(authenticationManager).isNotNull();
        assertThat(appUserRepository.findByEmail(user.getEmail()).get().getPassword())
                .isEqualTo(passwordEncoder.encode("qwerty"));
    }

    @Test
    void shouldThrowExceptionIfUserDoesNotExistInDatabase() {
        // given
        AppUserDetails userDetails = createAppUserDetails("Jan", "Kowalski", "jankowalski@example.com", "USER");
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, "user1", userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        AppUser user = createAppUser(2L, "Jan", "Kowalski", "jankowalski@example.com");
        when(appUserRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        // then
        assertThatThrownBy(() -> appUserLoginDataService.changePassword("user1", "qwerty"))
                .isInstanceOf(AppUserNotFoundException.class)
                .hasMessage("Current user doesn't exist in database.");
    }

    // ADMIN
    @Test
    void shouldThrowExceptionIfUserHasNotAdminRole() {
        // given
        AppUserDetails userDetails = createAppUserDetails("Jan", "Kowalski", "jankowalski@example.com", "USER");
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, "user1", userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        // when
        assertThatThrownBy(() -> appUserLoginDataService.changePassword(2L, "qwerty"))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("Can't change password as no admin role user found in context for current user.");
    }

    @Test
    void shouldChangePasswordIfUserHasAdminRole() {
        // given
        AppUserDetails userDetails = createAppUserDetails("Admin", "Admin", "admin@example.com", "ADMIN");
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, "admin", userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        AppUser user = createAppUser(2L, "Jan", "Kowalski", "jankowalski@example.com");
        when(appUserRepository.findById(user.getId())).thenReturn(Optional.of(user));
        // when
        appUserLoginDataService.changePassword(2L, "qwerty");
    }
}
