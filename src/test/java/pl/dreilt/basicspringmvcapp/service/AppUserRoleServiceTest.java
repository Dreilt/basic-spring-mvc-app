package pl.dreilt.basicspringmvcapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.dreilt.basicspringmvcapp.entity.AppUserRole;
import pl.dreilt.basicspringmvcapp.repository.AppUserRoleRepository;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.dreilt.basicspringmvcapp.core.AppUserHelper.*;

@ExtendWith(MockitoExtension.class)
class AppUserRoleServiceTest {
    @Mock
    private AppUserRoleRepository appUserRoleRepository;
    @InjectMocks
    private AppUserRoleService appUserRoleService;

    @Test
    void shouldGetAllUserRoles() {
        // given
        Set<AppUserRole> userRolesSet = Set.of(createAdminRole(), createOrganizerRole(), createUserRole());
        Mockito.when(appUserRoleRepository.findAll()).thenReturn(userRolesSet);
        // when
        Set<AppUserRole> userRoles = appUserRoleService.findAllUserRoles();
        // then
        assertThat(userRoles).isNotEmpty();
        assertThat(userRoles).hasSize(3);
        assertThat(userRoles).contains(userRolesSet.stream().toList().get(0));
        assertThat(userRoles).contains(userRolesSet.stream().toList().get(1));
        assertThat(userRoles).contains(userRolesSet.stream().toList().get(2));
    }
}
