package pl.dreilt.basicspringmvcapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.dreilt.basicspringmvcapp.dto.AppUserRegistrationDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;
import pl.dreilt.basicspringmvcapp.exception.NoSuchRoleException;
import pl.dreilt.basicspringmvcapp.repository.AppUserRoleRepository;
import pl.dreilt.basicspringmvcapp.repository.ProfileImageRepository;
import pl.dreilt.basicspringmvcapp.repository.RegistrationRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static pl.dreilt.basicspringmvcapp.core.AppUserHelper.createAppUser;
import static pl.dreilt.basicspringmvcapp.core.AppUserHelper.createUserRole;
import static pl.dreilt.basicspringmvcapp.service.RegistrationServiceTestHelper.createAppUserRegistrationDto;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {
    static final String USER_ROLE = "USER";
    @Mock
    private RegistrationRepository registrationRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ProfileImageRepository profileImageRepository;
    @Mock
    private AppUserRoleRepository appUserRoleRepository;
    @InjectMocks
    private RegistrationService registrationService;

    @Test
    void shouldReturnTrueIfAppUserExists() {
        // given
        AppUser user = createAppUser(2L, "Jan", "Kowalski", "jankowalski@example.com");
        when(registrationRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        // when
        boolean isAppUserExists = registrationService.checkIfAppUserExists(user.getEmail());
        // then
        assertThat(isAppUserExists).isTrue();
    }

    @Test
    void shouldReturnFalseIfAppUserDoesNotExists() {
        // given
        String email = "test@example.com";
        when(registrationRepository.findByEmail(email)).thenReturn(Optional.empty());
        // when
        boolean isAppUserExists = registrationService.checkIfAppUserExists(email);
        // then
        assertThat(isAppUserExists).isFalse();
    }

    @Test
    void shouldThrowExceptionIfUserRoleIsNotFound() {
        // given
        when(appUserRoleRepository.findRoleByName(USER_ROLE)).thenReturn(Optional.empty());
        // then
        assertThatThrownBy(() -> registrationService.register(createAppUserRegistrationDto("Jan", "Kowalski", "jankowalski@example.com")))
                .isInstanceOf(NoSuchRoleException.class)
                .hasMessage("Invalid role: " + USER_ROLE, USER_ROLE);
    }

    @Test
    void shouldRegisterUser() {
        // given
        AppUserRegistrationDto userRegistrationDto = createAppUserRegistrationDto("Jan", "Kowalski", "jankowalski@example.com");

        AppUser newUser = createAppUser(2L, userRegistrationDto.getFirstName(), userRegistrationDto.getLastName(), userRegistrationDto.getEmail());

        when(appUserRoleRepository.findRoleByName(USER_ROLE)).thenReturn(Optional.of(createUserRole()));

        when(registrationRepository.save(newUser)).thenReturn(newUser);

        // when
        registrationService.register(userRegistrationDto);
        // then
        Mockito.verify(registrationRepository, Mockito.times(1)).save(eq(newUser));
    }
}