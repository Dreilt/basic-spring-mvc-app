package pl.dreilt.basicspringmvcapp.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.dreilt.basicspringmvcapp.config.AppUserDetailsService;
import pl.dreilt.basicspringmvcapp.exception.NoSuchRoleException;
import pl.dreilt.basicspringmvcapp.pi.ProfileImageRepository;
import pl.dreilt.basicspringmvcapp.role.AppUserRoleRepository;
import pl.dreilt.basicspringmvcapp.user.dto.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static pl.dreilt.basicspringmvcapp.role.AppUserRoleServiceTestHelper.createOrganizerRole;
import static pl.dreilt.basicspringmvcapp.role.AppUserRoleServiceTestHelper.createUserRole;
import static pl.dreilt.basicspringmvcapp.user.AppUserServiceTestHelper.*;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {
    static final String USER_ROLE = "USER";
    static final PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.fromString("asc"), "lastName"));
    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ProfileImageRepository profileImageRepository;
    @Mock
    private AppUserRoleRepository appUserRoleRepository;
    @Mock
    private AppUserDetailsService appUserDetailsService;
    @InjectMocks
    private AppUserService appUserService;

    @Test
    void shouldReturnTrueIfAppUserExists() {
        // given
        AppUser user = createAppUser(2L, "Jan", "Kowalski", "jankowalski@example.com", createOrganizerRole());
        when(appUserRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        // when
        boolean isAppUserExists = appUserService.checkIfAppUserExists(user.getEmail());
        // then
        assertThat(isAppUserExists).isTrue();
    }

    @Test
    void shouldReturnFalseIfAppUserDoesNotExists() {
        // given
        String email = "test@example.com";
        when(appUserRepository.findByEmail(email)).thenReturn(Optional.empty());
        // when
        boolean isAppUserExists = appUserService.checkIfAppUserExists(email);
        // then
        assertThat(isAppUserExists).isFalse();
    }

    @Test
    void shouldThrowExceptionIfUserRoleIsNotFound() {
        // given
        when(appUserRoleRepository.findRoleByName(USER_ROLE)).thenReturn(Optional.empty());
        // then
        assertThatThrownBy(() -> appUserService.register(createAppUserRegistrationDto("Jan", "Kowalski", "jankowalski@example.com")))
                .isInstanceOf(NoSuchRoleException.class)
                .hasMessage("Invalid role: " + USER_ROLE, USER_ROLE);
    }

    @Test
    void shouldRegisterUser() {
        // given
        AppUserRegistrationDto userRegistrationDto = createAppUserRegistrationDto("Jan", "Kowalski", "jankowalski@example.com");
        when(appUserRoleRepository.findRoleByName(USER_ROLE)).thenReturn(Optional.of(createUserRole()));
        // when
        appUserService.register(userRegistrationDto);
        // then
        verify(appUserRepository, times(1)).save(argThat((AppUser saved) -> {
            Assertions.assertAll("Testing saved user",
                    () -> assertNull(saved.getId()),
                    () -> assertEquals(userRegistrationDto.getFirstName(), saved.getFirstName()),
                    () -> assertEquals(userRegistrationDto.getLastName(), saved.getLastName()),
                    () -> assertEquals(userRegistrationDto.getEmail(), saved.getEmail())
            );
            return true;
        }));
    }

    @Test
    void shouldGetAllUsers() {
        // given
        List<AppUser> userList = createAppUserList();
        when(appUserRepository.findAllUsers(pageRequest)).thenReturn(new PageImpl<>(userList, pageRequest, userList.size()));
        // when
        Page<AppUserTableAPDto> users = appUserService.findAllUsers(pageRequest);
        // then
        assertThat(users).isNotEmpty();
        assertThat(users).hasSize(4);
        assertThat(users.getContent().get(0).getLastName()).isEqualTo("Admin");
        assertThat(users.getContent().get(1).getLastName()).isEqualTo("Kowalski");
        assertThat(users.getContent().get(2).getLastName()).isEqualTo("Kowalski");
        assertThat(users.getContent().get(3).getLastName()).isEqualTo("Nowak");
    }

    @Test
    void shouldGetEmptyPageOfUsersIfSearchQueryIsEmpty() {
        // given
        String searchQuery = "";
        // when
        Page<AppUserTableAPDto> users = appUserService.findUsersBySearch(searchQuery, pageRequest);
        // then
        assertThat(users).isEmpty();
    }

    @Test
    void shouldGetUsersBySearchIfSearchQueryHasOneWord() {
        // given
        String searchQuery = "jan";
        when(appUserRepository.findAll(any(Specification.class), eq(pageRequest))).thenReturn(new PageImpl<>(createAppUserListBySearch(searchQuery)));
        // when
        Page<AppUserTableAPDto> users = appUserService.findUsersBySearch(searchQuery, pageRequest);
        // then
        assertThat(users).isNotEmpty();
        assertThat(users).hasSize(2);
        assertThat(users.getContent().get(0).getLastName()).isEqualTo("Kowalski");
        assertThat(users.getContent().get(1).getLastName()).isEqualTo("Nowak");
    }

    @Test
    void shouldGetUsersBySearchIfSearchQueryHasTwoWord() {
        // given
        String searchQuery = "jan kowalski";
        when(appUserRepository.findAll(any(Specification.class), eq(pageRequest))).thenReturn(new PageImpl<>(createAppUserListBySearch(searchQuery)));
        // when
        Page<AppUserTableAPDto> users = appUserService.findUsersBySearch(searchQuery, pageRequest);
        // then
        assertThat(users).isNotEmpty();
        assertThat(users).hasSize(1);
        assertThat(users.getContent().get(0).getFirstName()).isEqualTo("Jan");
        assertThat(users.getContent().get(0).getLastName()).isEqualTo("Kowalski");
    }

    @Test
    void shouldGetUserProfileByUserId() {
        // given
        AppUser user = createAppUser(3L, "Patryk", "Kowalski", "patrykkowalski@example.com", createUserRole());
        when(appUserRepository.findById(user.getId())).thenReturn(Optional.of(user));
        // when
        AppUserProfileDto userProfile = appUserService.findUserProfileByUserId(user.getId());
        // then
        assertThat(userProfile).isNotNull();
    }

    @Test
    void shouldGetUserProfile() {
        // given
        AppUser user = createAppUser(2L, "Jan", "Kowalski", "jankowalski@example.com", createOrganizerRole());
        // when
        AppUserProfileDto userProfile = appUserService.findUserProfile(user);
        // then
        assertThat(userProfile).isNotNull();
        assertThat(userProfile.getFirstName()).isEqualTo("Jan");
        assertThat(userProfile.getLastName()).isEqualTo("Kowalski");
        assertThat(userProfile.getEmail()).isEqualTo("jankowalski@example.com");
        assertThat(userProfile.getBio()).isNull();
        assertThat(userProfile.getCity()).isNull();
    }

    @Test
    void shouldGetUserProfileToEdit() {
        // given
        AppUser user = createAppUser(2L, "emptyFirstName", "emptyLastName", "jankowalski@example.com", createOrganizerRole());
        // when
        AppUserProfileEditDto userProfileToEdit = appUserService.findUserProfileToEdit(user);
        // then
        assertThat(userProfileToEdit).isNotNull();
        assertThat(userProfileToEdit.getFirstName()).isEqualTo("emptyFirstName");
        assertThat(userProfileToEdit.getLastName()).isEqualTo("emptyLastName");
        assertThat(userProfileToEdit.getProfileImage()).isNull();
        assertThat(userProfileToEdit.getBio()).isNull();
        assertThat(userProfileToEdit.getCity()).isNull();
    }

    @Test
    void shouldGetUpdatedUserProfile() {
        // given
        AppUser user = createAppUser(2L, "emptyFirstName", "emptyLastName", "jankowalski@example.com", createOrganizerRole());
        // when
        AppUserProfileEditDto userProfileUpdated = appUserService.updateUserProfile(user, createAppUserProfileEditDto());
        // then
        assertThat(userProfileUpdated.getFirstName()).isEqualTo("Jan");
        assertThat(userProfileUpdated.getLastName()).isEqualTo("Kowalski");
        assertThat(userProfileUpdated.getProfileImage()).isNull();
        assertThat(userProfileUpdated.getBio()).isEqualTo("Cześć! Nazywam się Jan Kowalski i niedawno przeprowadziłem się do Krakowa.");
        assertThat(userProfileUpdated.getCity()).isEqualTo("Kraków");
    }

    @Test
    void shouldGetUserAccountDataToEditAP() {
        // given
        AppUser user = createAppUser(2L, "Jan", "Kowalski", "jankowalski@example.com", createOrganizerRole());
        when(appUserRepository.findById(user.getId())).thenReturn(Optional.of(user));
        // when
        AppUserAccountEditAPDto userAccountData = appUserService.findUserAccountToEdit(user.getId());
        // then
        assertThat(userAccountData).isNotNull();
        assertThat(userAccountData.isEnabled()).isTrue();
        assertThat(userAccountData.isAccountNonLocked()).isTrue();
        assertThat(userAccountData.getRoles()).hasSize(1);
    }

    @Test
    void shouldUpdateUserAccountAP() {
        // given
        AppUser user = createAppUser(2L, "Jan", "Kowalski", "jankowalski@example.com", createOrganizerRole());
        when(appUserRepository.findById(user.getId())).thenReturn(Optional.of(user));
        // when
        AppUserAccountEditAPDto userAccountUpdated = appUserService.updateUserAccount(user.getId(), createAppUserAccountEditAPDto());
        // then
        assertThat(userAccountUpdated).isNotNull();
        assertThat(userAccountUpdated.isEnabled()).isFalse();
        assertThat(userAccountUpdated.isAccountNonLocked()).isFalse();
        assertThat(userAccountUpdated.getRoles()).hasSize(1);
        assertThat(userAccountUpdated.getRoles().stream().toList().get(0).getName()).isEqualTo("ORGANIZER");
    }

    @Test
    void shouldGetUserProfileToEditAP() {
        // given
        AppUser user = createAppUser(1L, "emptyFirstName", "emptyLastName", "jankowalski@example.com", createOrganizerRole());
        when(appUserRepository.findById(user.getId())).thenReturn(Optional.of(user));
        // when
        AppUserProfileEditAPDto userProfile = appUserService.findUserProfileToEdit(user.getId());
        // then
        assertThat(userProfile).isNotNull();
        assertThat(userProfile.getFirstName()).isEqualTo("emptyFirstName");
        assertThat(userProfile.getLastName()).isEqualTo("emptyLastName");
        assertThat(userProfile.getBio()).isNull();
        assertThat(userProfile.getCity()).isNull();
    }

    @Test
    void shouldGetUpdatedUserProfileAP() {
        // given
        AppUser user = createAppUser(1L, "emptyFirstName", "emptyLastName", "jankowalski@example.com", createOrganizerRole());
        when(appUserRepository.findById(user.getId())).thenReturn(Optional.of(user));
        // when
        AppUserProfileEditAPDto userProfileUpdated = appUserService.updateUserProfile(user.getId(), createAppUserProfileEditAPDto());
        // then
        assertThat(userProfileUpdated).isNotNull();
        assertThat(userProfileUpdated.getFirstName()).isEqualTo("Jan");
        assertThat(userProfileUpdated.getLastName()).isEqualTo("Kowalski");
        assertThat(userProfileUpdated.getBio()).isEqualTo("Cześć! Nazywam się Jan Kowalski i niedawno przeprowadziłem się do Krakowa.");
        assertThat(userProfileUpdated.getCity()).isEqualTo("Kraków");
    }

    @Test
    void shouldDeleteUser() {
        // given
        Long userId = 1L;
        // when
        appUserService.deleteUser(userId);
        // then
        verify(appUserRepository, times(1)).deleteById(eq(userId));
    }
}