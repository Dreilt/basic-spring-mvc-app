package pl.dreilt.basicspringmvcapp.service;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import pl.dreilt.basicspringmvcapp.dto.AppUserAccountEditAdminPanelDto;
import pl.dreilt.basicspringmvcapp.dto.AppUserAdminPanelDto;
import pl.dreilt.basicspringmvcapp.dto.AppUserProfileEditAdminPanelDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;
import pl.dreilt.basicspringmvcapp.entity.AppUserRole;
import pl.dreilt.basicspringmvcapp.exception.AppUserNotFoundException;
import pl.dreilt.basicspringmvcapp.repository.AdminRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.*;
import static pl.dreilt.basicspringmvcapp.service.AdminServiceTestHelper.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {
    static final PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.fromString("asc"), "lastName"));
    @Mock
    private AdminRepository adminRepository;
    @InjectMocks
    private AdminService adminService;

    @Test
    void shouldGetAllUsers() {
        // given
        List<AppUser> userList = createUserList();
        when(adminRepository.findAllUsers(pageRequest)).thenReturn(new PageImpl<>(userList, pageRequest, userList.size()));
        // when
        Page<AppUserAdminPanelDto> users = adminService.findAllUsers(pageRequest);
        // then
        assertThat(users)
                .isNotEmpty()
                .hasSize(3);
        assertThat(users.getContent()).extracting(AppUserAdminPanelDto::getLastName).contains("Kowalski", "Nowak");
    }

    @Test
    void shouldGetEmptyPageOfUsersWhenSearchQueryIsEmpty() {
        // given
        String searchQuery = "";
        // when
        Page<AppUserAdminPanelDto> users = adminService.findUsersBySearch(searchQuery, pageRequest);
        // then
        assertThat(users).isEmpty();
    }

    @Test
    void shouldGetUsersBySearchIfSearchQueryHasOneWord() {
        // given
        String searchQuery = "jan";
        when(adminRepository.findUsersBySearch(searchQuery, pageRequest)).thenReturn(new PageImpl<>(createUserListBySearch(searchQuery)));
        // when
        Page<AppUserAdminPanelDto> users = adminService.findUsersBySearch(searchQuery, pageRequest);
        // then
        assertThat(users)
                .isNotEmpty()
                .hasSize(2);
        assertThat(users.getContent()).extracting(AppUserAdminPanelDto::getLastName).contains("Kowalski", "Nowak");
    }

    @Test
    void shouldGetUsersBySearchIfSearchQueryHasTwoWord() {
        // given
        String searchQuery = "jan kowalski";
        String[] searchWords = searchQuery.split(" ");
        when(adminRepository.findUsersBySearch(searchWords[0], searchWords[1], pageRequest)).thenReturn(new PageImpl<>(createUserListBySearch(searchQuery)));
        // when
        Page<AppUserAdminPanelDto> users = adminService.findUsersBySearch(searchQuery, pageRequest);
        // then
        assertThat(users)
                .isNotEmpty()
                .hasSize(1);
        assertThat(users.getContent())
                .extracting("firstName", "lastName")
                .contains(tuple("Jan", "Kowalski"))
                .doesNotContain(tuple("Jan", "Nowak"));
    }

    @Test
    void shouldGetUserAccountToEdit() {
        // given
        Long userId = 1L;
        when(adminRepository.findById(userId)).thenReturn(Optional.of(createUser()));
        // when
        AppUserAccountEditAdminPanelDto userAccount = adminService.findUserAccountToEdit(userId);
        // then
        assertThat(userAccount).isNotNull();
        assertThat(userAccount.isEnabled()).isFalse();
        assertThat(userAccount.isAccountNonLocked()).isTrue();
        assertThat(userAccount.getRoles()).hasSize(1);
    }

    @Test
    void shouldThrowExceptionIfUserIsNotFoundWhileSearchingAccount() {
        // given
        Long userId = 2L;
        // then
        assertThatThrownBy(() -> adminService.findUserAccountToEdit(userId))
                .isInstanceOf(AppUserNotFoundException.class)
                .hasMessage("User with ID " + userId + " not found");
    }

    @Test
    void shouldUpdateUserAccount() {
        // given
        Long userId = 1L;
        when(adminRepository.findById(userId)).thenReturn(Optional.of(createUser()));
        // when
        AppUserAccountEditAdminPanelDto updated = adminService.updateUserAccount(userId, createUserAccountEditAdminPanelDto());
        // then
        assertThat(updated).isNotNull();
        assertThat(updated.isEnabled()).isTrue();
        assertThat(updated.isAccountNonLocked()).isFalse();
        assertThat(updated.getRoles()).hasSize(1)
//                .is(new Condition<>((r) -> r.contains(createUserRole()), "user roles should contain a defined role."));
                .extracting("name", "visibleName")
                .contains(tuple("USER", "Użytkownik"));
    }

    @Test
    void shouldThrowExceptionIfUserIsNotFoundWhileUpdatingAccount() {
        // given
        Long userId = 2L;
        // and:
        AppUserAccountEditAdminPanelDto usr = createUserAccountEditAdminPanelDto();
        // then
        assertThatThrownBy(() -> adminService.updateUserAccount(userId, usr))
                .isInstanceOf(AppUserNotFoundException.class)
                .hasMessage("User with ID " + userId + " not found");
    }

    @Test
    void shouldGetUserProfileToEdit() {
        // given
        Long userId = 1L;
        when(adminRepository.findById(userId)).thenReturn(Optional.of(createUser()));
        // when
        AppUserProfileEditAdminPanelDto userProfile = adminService.findUserProfileToEdit(userId);
        // then
        assertThat(userProfile).isNotNull();
        assertThat(userProfile.getFirstName()).isEqualTo("Test");
        assertThat(userProfile.getLastName()).isEqualTo("Test");
        assertThat(userProfile.getBio()).isEqualTo("Cześć! Nazywam się Test Test i jestem z Rzeszowa. Obecnie jestem na trzecim roku informatyki na Politechnice Rzeszowskiej.");
        assertThat(userProfile.getCity()).isEqualTo("Rzeszów");
    }

    @Test
    void shouldThrowExceptionIfUserIsNotFoundWhileSearchingProfile() {
        // given
        Long userId = 2L;
        // then
        assertThatThrownBy(() -> adminService.findUserProfileToEdit(userId))
                .isInstanceOf(AppUserNotFoundException.class)
                .hasMessage("User with ID " + userId + " not found");
    }

    @Test
    void shouldUpdateUserProfile() {
        // given
        Long userId = 1L;
        when(adminRepository.findById(userId)).thenReturn(Optional.of(createUser()));
        // when
        AppUserProfileEditAdminPanelDto userProfileUpdated = adminService.updateUserProfile(userId, createUserProfileEditAdminPanelDto());
        // then
        assertThat(userProfileUpdated).isNotNull();
        assertThat(userProfileUpdated.getFirstName()).isEqualTo("Jan");
        assertThat(userProfileUpdated.getLastName()).isEqualTo("Kowalski");
        assertThat(userProfileUpdated.getBio()).isEqualTo("Cześć! Nazywam się Jan Kowalski i niedawno przeprowadziłem się do Krakowa.");
        assertThat(userProfileUpdated.getCity()).isEqualTo("Kraków");
    }

    @Test
    void shouldThrowExceptionIfUserIsNotFoundWhileUpdatingProfile() {
        // given
        Long userId = 2L;
        // and:
        AppUserProfileEditAdminPanelDto usr = createUserProfileEditAdminPanelDto();
        // then
        assertThatThrownBy(() -> adminService.updateUserProfile(userId, usr))
                .isInstanceOf(AppUserNotFoundException.class)
                .hasMessage("User with ID " + userId + " not found");
    }

    @Test
    void shouldDeleteUser() {
        // given
        Long userId = 1L;
        // when
        adminService.deleteUser(userId);
        // then
        verify(adminRepository, times(1)).deleteById(userId);
    }
}