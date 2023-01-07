package pl.dreilt.basicspringmvcapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.dreilt.basicspringmvcapp.dto.AppUserProfileDto;
import pl.dreilt.basicspringmvcapp.dto.AppUserProfileEditDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;
import pl.dreilt.basicspringmvcapp.exception.AppUserNotFoundException;
import pl.dreilt.basicspringmvcapp.repository.AppUserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static pl.dreilt.basicspringmvcapp.core.AppUserHelper.*;
import static pl.dreilt.basicspringmvcapp.service.AppUserServiceTestHelper.createAppUserDetails;
import static pl.dreilt.basicspringmvcapp.service.AppUserServiceTestHelper.createAppUserProfileDataEditDto;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {
    @Mock
    private AppUserRepository appUserRepository;
    @InjectMocks
    private AppUserService appUserService;

    @Test
    void shouldGetUserProfile() {
        // given
        AppUser user = createAppUser(2L, "Jan", "Kowalski", "jankowalski@example.com", createOrganizerRole());
        when(appUserRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        // when
        AppUserProfileDto userProfile = appUserService.findUserProfile(user.getEmail());
        // then
        assertThat(userProfile).isNotNull();
        assertThat(userProfile.getFirstName()).isEqualTo("Jan");
        assertThat(userProfile.getLastName()).isEqualTo("Kowalski");
        assertThat(userProfile.getEmail()).isEqualTo("jankowalski@example.com");
        assertThat(userProfile.getBio()).isNull();
        assertThat(userProfile.getCity()).isNull();
    }

    @Test
    void shouldThrowExceptionIfUserIsNotFound() {
        // given
        String email = "test@example.com";
        when(appUserRepository.findByEmail(email)).thenReturn(Optional.empty());
        // then
        assertThatThrownBy(() -> appUserService.findUserProfile(email))
                .isInstanceOf(AppUserNotFoundException.class)
                .hasMessage("User with email " + email + " not found");
    }

    @Test
    void shouldGetUserProfileDataToEdit() {
        // given
        AppUser user = createAppUser(1L, "emptyFirstName", "emptyLastName", "jankowalski@example.com", createOrganizerRole());
        when(appUserRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        // when
        AppUserProfileEditDto userProfileToEdit = appUserService.findUserProfileToEdit(user.getEmail());
        // then
        assertThat(userProfileToEdit).isNotNull();
        assertThat(userProfileToEdit.getFirstName()).isEqualTo("emptyFirstName");
        assertThat(userProfileToEdit.getLastName()).isEqualTo("emptyLastName");
        assertThat(userProfileToEdit.getProfileImage()).isNull();
        assertThat(userProfileToEdit.getBio()).isNull();
        assertThat(userProfileToEdit.getCity()).isNull();
    }

    @Test
    void shouldThrowExceptionIfUserIsNotFoundWhileSearchingProfileToEdit() {
        // given
        String email = "test@example.com";
        when(appUserRepository.findByEmail(email)).thenReturn(Optional.empty());
        // then
        assertThatThrownBy(() -> appUserService.findUserProfileToEdit(email))
                .isInstanceOf(AppUserNotFoundException.class)
                .hasMessage("User with email " + email + " not found");
    }

    @Test
    void shouldGetUpdatedUserProfileData() {
        // given
        Authentication auth = new UsernamePasswordAuthenticationToken(createAppUserDetails("emptyFirstName", "emptyLastName", "jankowalski@example.com", "USER"), null);
        SecurityContextHolder.getContext().setAuthentication(auth);
        AppUser user = createAppUser(1L, "emptyFirstName", "emptyLastName", "jankowalski@example.com", createOrganizerRole());
        when(appUserRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        // when
        AppUserProfileEditDto userProfileUpdated = appUserService.updateUserProfile(createAppUserProfileDataEditDto());
        // then
        assertThat(userProfileUpdated.getFirstName()).isEqualTo("Jan");
        assertThat(userProfileUpdated.getLastName()).isEqualTo("Kowalski");
        assertThat(userProfileUpdated.getProfileImage()).isNull();
        assertThat(userProfileUpdated.getBio()).isEqualTo("Cześć! Nazywam się Jan Kowalski i niedawno przeprowadziłem się do Krakowa.");
        assertThat(userProfileUpdated.getCity()).isEqualTo("Kraków");
    }

    @Test
    void shouldGetUserProfileByUserId() {
        // given
        AppUser user = createAppUser(2L, "Patryk", "Kowalski", "patrykkowalski@example.com", createUserRole());
        when(appUserRepository.findById(user.getId())).thenReturn(Optional.of(user));
        // when
        AppUserProfileDto userProfile = appUserService.findUserProfileByUserId(user.getId());
        // then
        assertThat(userProfile).isNotNull();
    }
}
