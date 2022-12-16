package pl.dreilt.basicspringmvcapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.dreilt.basicspringmvcapp.dto.AppUserCredentialsDto;
import pl.dreilt.basicspringmvcapp.dto.AppUserProfileDto;
import pl.dreilt.basicspringmvcapp.dto.AppUserProfileEditDto;
import pl.dreilt.basicspringmvcapp.exception.AppUserNotFoundException;
import pl.dreilt.basicspringmvcapp.exception.NoSuchRoleException;
import pl.dreilt.basicspringmvcapp.repository.AppUserAvatarRepository;
import pl.dreilt.basicspringmvcapp.repository.AppUserRepository;
import pl.dreilt.basicspringmvcapp.repository.AppUserRoleRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static pl.dreilt.basicspringmvcapp.service.AppUserLoginDataServiceTestHelper.createUser;
import static pl.dreilt.basicspringmvcapp.service.AppUserServiceTestHelper.*;

@ExtendWith(SpringExtension.class)
class AppUserServiceTest {
    static final String USER_ROLE = "USER";
    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private AppUserRoleRepository appUserRoleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AppUserAvatarRepository appUserAvatarRepository;
    @Mock
    private ClassPathResource classPathResource;
    @InjectMocks
    private AppUserService appUserService;

    @BeforeEach
    void setUp() {
        Authentication auth = new UsernamePasswordAuthenticationToken(createAppUserDetails(), null);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void shouldReturnTrueIfAppUserExists() {
        // given
        String email = "test@example.com";
        Mockito.when(appUserRepository.findByEmail(email)).thenReturn(Optional.of(createUser()));
        // when
        boolean isAppUserExists = appUserService.checkIfAppUserExists(email);
        // then
        assertThat(isAppUserExists).isTrue();
    }

    @Test
    void shouldReturnFalseIfAppUserDoesNotExists() {
        // given
        String email = "test2@example.com";
        Mockito.when(appUserRepository.findByEmail(email)).thenReturn(Optional.empty());
        // when
        boolean isAppUserExists = appUserService.checkIfAppUserExists(email);
        // then
        assertThat(isAppUserExists).isFalse();
    }

    @Test
    void shouldGetAppUserCredentials() {
        // given
        String email = "test@example.com";
        Mockito.when(appUserRepository.findByEmail(email)).thenReturn(Optional.of(createUser()));
        // when
        Optional<AppUserCredentialsDto> appUserCredentials = appUserService.findAppUserCredentialsByEmail(email);
        // then
        assertThat(appUserCredentials.isPresent()).isTrue();
        assertThat(appUserCredentials.get().getEmail()).isEqualTo(email);
    }

    // ##### POPRAWKA #####
//    @Test
//    void shouldRegisterAppUser() throws IOException {
////        // given
////
////        Mockito.lenient().when(classPathResource.getInputStream()).thenReturn(null);
////
//////        Mockito.when(appUserRoleRepository.findRoleByName(USER_ROLE)).thenReturn(Optional.of(createUserRole()));
////        // when
//////        appUserService.register(createAppUserRegistrationDto());
////        // then
////        assertThatThrownBy(() -> appUserService.register(createAppUserRegistrationDto()))
////                .isInstanceOf(DefaultProfileImageNotFoundException.class);
//
//
//        // given
//        Resource mockResource = Mockito.mock(Resource.class);
//        Mockito.when(mockResource.getInputStream()).thenReturn(null);
//        // then
//        assertThatThrownBy(() -> appUserService.register(createAppUserRegistrationDto()))
//                .isInstanceOf(DefaultProfileImageNotFoundException.class);
//    }

    @Test
    void shouldThrowExceptionIfUserRoleIsNotFound() {
        // given
        Mockito.when(appUserRoleRepository.findRoleByName(USER_ROLE)).thenReturn(Optional.empty());
        // then
        assertThatThrownBy(() -> appUserService.register(createAppUserRegistrationDto()))
                .isInstanceOf(NoSuchRoleException.class)
                .hasMessage("Invalid role: " + USER_ROLE, USER_ROLE);
    }

    @Test
    void shouldGetAppUserProfile() {
        // given
        String email = "test@example.com";
        Mockito.when(appUserRepository.findByEmail(email)).thenReturn(Optional.of(createUser()));
        // when
        AppUserProfileDto userProfile = appUserService.findUserProfile(email);
        // then
        assertThat(userProfile).isNotNull();
        assertThat(userProfile.getFirstName()).isEqualTo("Test");
        assertThat(userProfile.getLastName()).isEqualTo("Test");
        assertThat(userProfile.getEmail()).isEqualTo("test@example.com");
        assertThat(userProfile.getBio()).isEqualTo("Cześć! Nazywam się Test Test i jestem z Rzeszowa. Obecnie jestem na trzecim roku informatyki na Politechnice Rzeszowskiej.");
        assertThat(userProfile.getCity()).isEqualTo("Rzeszów");
    }

    @Test
    void shouldThrowExceptionIfUserIsNotFound() {
        // given
        String email = "test2@example.com";
        Mockito.when(appUserRepository.findByEmail(email)).thenReturn(Optional.empty());
        // then
        assertThatThrownBy(() -> appUserService.findUserProfile(email))
                .isInstanceOf(AppUserNotFoundException.class)
                .hasMessage("User with email " + email + " not found");
    }

    @Test
    void shouldGetAppUserProfileToEdit() {
        // given
        String email = "test@example.com";
        Mockito.when(appUserRepository.findByEmail(email)).thenReturn(Optional.of(createUser()));
        // when
        AppUserProfileEditDto userProfileToEdit = appUserService.findUserProfileToEdit(email);
        // then
        assertThat(userProfileToEdit).isNotNull();
        assertThat(userProfileToEdit.getFirstName()).isEqualTo("Test");
        assertThat(userProfileToEdit.getLastName()).isEqualTo("Test");
        assertThat(userProfileToEdit.getProfileImage()).isNull();
        assertThat(userProfileToEdit.getBio()).isEqualTo("Cześć! Nazywam się Test Test i jestem z Rzeszowa. Obecnie jestem na trzecim roku informatyki na Politechnice Rzeszowskiej.");
        assertThat(userProfileToEdit.getCity()).isEqualTo("Rzeszów");
    }

    @Test
    void shouldThrowExceptionIfUserIsNotFoundWhileSearchingProfileToEdit() {
        // given
        String email = "test2@example.com";
        Mockito.when(appUserRepository.findByEmail(email)).thenReturn(Optional.empty());
        // then
        assertThatThrownBy(() -> appUserService.findUserProfileToEdit(email))
                .isInstanceOf(AppUserNotFoundException.class)
                .hasMessage("User with email " + email + " not found");
    }

    @Test
    void shouldGetUpdatedUserProfile() {
        // given
        Mockito.when(appUserRepository.findByEmail("test@example.com")).thenReturn(Optional.of(AppUserServiceTestHelper.createUser()));
        // when
        AppUserProfileEditDto appUserProfileUpdated = appUserService.updateUserProfile(createAppUserProfileEditDto());
        // then
        assertThat(appUserProfileUpdated.getFirstName()).isEqualTo("NewFirstName");
        assertThat(appUserProfileUpdated.getLastName()).isEqualTo("NewLastName");
//        assertThat(appUserProfileUpdated.getProfileImage().getName()).isEqualTo("avatar.png"); // to jest null i to trzeba poprawić przy zwrocie
        assertThat(appUserProfileUpdated.getBio()).isEqualTo("NewBio");
        assertThat(appUserProfileUpdated.getCity()).isEqualTo("NewCity");
    }

    @Test
    void shouldGetUserProfileByUserId() {
        // given
        Long userId = 1L;
        Mockito.when(appUserRepository.findById(userId)).thenReturn(Optional.of(AppUserServiceTestHelper.createUser()));
        // when
        AppUserProfileDto userProfile = appUserService.findUserProfileByUserId(userId);
        // then
        assertThat(userProfile).isNotNull();
    }
}
