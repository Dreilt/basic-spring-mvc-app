package pl.dreilt.basicspringmvcapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import pl.dreilt.basicspringmvcapp.dto.AppUserAccountDataEditAPDto;
import pl.dreilt.basicspringmvcapp.dto.AppUserProfileDataEditAPDto;
import pl.dreilt.basicspringmvcapp.dto.AppUserTableAPDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;
import pl.dreilt.basicspringmvcapp.exception.AppUserNotFoundException;
import pl.dreilt.basicspringmvcapp.repository.AdminRepository;
import pl.dreilt.basicspringmvcapp.specification.AppUserSpecification;
import pl.dreilt.basicspringmvcapp.specification.SearchCriteria;
import pl.dreilt.basicspringmvcapp.specification.SearchOperation;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static pl.dreilt.basicspringmvcapp.core.AppUserHelper.createAppUser;
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
        List<AppUser> userList = createAppUserList();
        when(adminRepository.findAllUsers(pageRequest)).thenReturn(new PageImpl<>(userList, pageRequest, userList.size()));
        // when
        Page<AppUserTableAPDto> users = adminService.findAllUsers(pageRequest);
        // then
        assertThat(users).isNotEmpty();
        assertThat(users).hasSize(3);
        assertThat(users.getContent().get(0).getLastName()).isEqualTo("Kowalski");
        assertThat(users.getContent().get(1).getLastName()).isEqualTo("Kowalski");
        assertThat(users.getContent().get(2).getLastName()).isEqualTo("Nowak");
    }

    @Test
    void shouldGetEmptyPageOfUsersIfSearchQueryIsEmpty() {
        // given
        String searchQuery = "";
        // when
        Page<AppUserTableAPDto> users = adminService.findUsersBySearch(searchQuery, pageRequest);
        // then
        assertThat(users).isEmpty();
    }

    @Test
    void shouldGetUsersBySearchIfSearchQueryHasOneWord() {
        // given
        String searchQuery = "jan";
        when(adminRepository.findAll(AppUserSpecification.bySearch(searchQuery), pageRequest)).thenReturn(new PageImpl<>(createAppUserListBySearch(searchQuery)));
        // when
        Page<AppUserTableAPDto> users = adminService.findUsersBySearch(searchQuery, pageRequest);
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
        String[] searchWords = searchQuery.split(" ");
        when(adminRepository.findAll(AppUserSpecification.bySearch(searchWords[0], searchWords[1]), pageRequest)).thenReturn(new PageImpl<>(createAppUserListBySearch(searchQuery)));
        // when
        Page<AppUserTableAPDto> users = adminService.findUsersBySearch(searchQuery, pageRequest);
        // then
        assertThat(users).isNotEmpty();
        assertThat(users).hasSize(1);
        assertThat(users.getContent().get(0).getFirstName()).isEqualTo("Jan");
        assertThat(users.getContent().get(0).getLastName()).isEqualTo("Kowalski");
    }

    @Test
    void shouldGetUserAccountDataToEdit() {
        // given
        AppUser user = createAppUser(1L, "Jan", "Kowalski", "jankowalski@example.com");
        when(adminRepository.findById(user.getId())).thenReturn(Optional.of(user));
        // when
        AppUserAccountDataEditAPDto userAccountData = adminService.findUserAccountDataToEdit(user.getId());
        // then
        assertThat(userAccountData).isNotNull();
        assertThat(userAccountData.isEnabled()).isTrue();
        assertThat(userAccountData.isAccountNonLocked()).isTrue();
        assertThat(userAccountData.getRoles()).hasSize(1);
    }

    @Test
    void shouldThrowExceptionIfUserIsNotFoundWhileSearchingAccountData() {
        // given
        Long userId = 2L;
        // then
        assertThatThrownBy(() -> adminService.findUserAccountDataToEdit(userId))
                .isInstanceOf(AppUserNotFoundException.class)
                .hasMessage("User with ID " + userId + " not found");
    }

    @Test
    void shouldUpdateUserAccountData() {
        // given
        AppUser user = createAppUser(1L, "Jan", "Kowalski", "jankowalski@example.com");
        when(adminRepository.findById(user.getId())).thenReturn(Optional.of(user));
        // when
        AppUserAccountDataEditAPDto userAccountUpdated = adminService.updateUserAccountData(user.getId(), createAppUserAccountDataEditAPDto());
        // then
        assertThat(userAccountUpdated).isNotNull();
        assertThat(userAccountUpdated.isEnabled()).isFalse();
        assertThat(userAccountUpdated.isAccountNonLocked()).isFalse();
        assertThat(userAccountUpdated.getRoles()).hasSize(1);
        assertThat(userAccountUpdated.getRoles().stream().toList().get(0).getName()).isEqualTo("ORGANIZER");
    }

    @Test
    void shouldThrowExceptionIfUserIsNotFoundWhileUpdatingHisAccountData() {
        // given
        Long userId = 2L;
        // then
        assertThatThrownBy(() -> adminService.updateUserAccountData(userId, createAppUserAccountDataEditAPDto()))
                .isInstanceOf(AppUserNotFoundException.class)
                .hasMessage("User with ID " + userId + " not found");
    }

    @Test
    void shouldGetUserProfileDataToEdit() {
        // given
        AppUser user = createAppUser(1L, "emptyFirstName", "emptyLastName", "jankowalski@example.com");
        when(adminRepository.findById(user.getId())).thenReturn(Optional.of(user));
        // when
        AppUserProfileDataEditAPDto userProfile = adminService.findUserProfileDataToEdit(user.getId());
        // then
        assertThat(userProfile).isNotNull();
        assertThat(userProfile.getFirstName()).isEqualTo("emptyFirstName");
        assertThat(userProfile.getLastName()).isEqualTo("emptyLastName");
        assertThat(userProfile.getBio()).isNull();
        assertThat(userProfile.getCity()).isNull();
    }

    @Test
    void shouldThrowExceptionIfUserIsNotFoundWhileSearchingHisProfileData() {
        // given
        Long userId = 2L;
        // then
        assertThatThrownBy(() -> adminService.findUserProfileDataToEdit(userId))
                .isInstanceOf(AppUserNotFoundException.class)
                .hasMessage("User with ID " + userId + " not found");
    }

    @Test
    void shouldGetUpdatedUserProfileData() {
        // given
        AppUser user = createAppUser(1L, "emptyFirstName", "emptyLastName", "jankowalski@example.com");
        when(adminRepository.findById(user.getId())).thenReturn(Optional.of(user));
        // when
        AppUserProfileDataEditAPDto userProfileUpdated = adminService.updateUserProfile(user.getId(), createAppUserProfileDataEditAPDto());
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
        // then
        assertThatThrownBy(() -> adminService.updateUserProfile(userId, createAppUserProfileDataEditAPDto()))
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
        Mockito.verify(adminRepository, Mockito.times(1)).deleteById(eq(userId));
    }
}