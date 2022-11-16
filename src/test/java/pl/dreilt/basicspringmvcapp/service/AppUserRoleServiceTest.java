package pl.dreilt.basicspringmvcapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.dreilt.basicspringmvcapp.entity.AppUserRole;
import pl.dreilt.basicspringmvcapp.repository.AppUserRoleRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.dreilt.basicspringmvcapp.service.AppUserRoleServiceTestHelper.createAdminRole;
import static pl.dreilt.basicspringmvcapp.service.AppUserRoleServiceTestHelper.createUserRole;

@ExtendWith(MockitoExtension.class)
class AppUserRoleServiceTest {

    @Mock
    private AppUserRoleRepository appUserRoleRepository;

    @InjectMocks
    private AppUserRoleService appUserRoleService;

    @BeforeEach
    void setUp() {
        AppUserRole[] userRolesArray = {
                createAdminRole(),
                createUserRole()
        };
        Set<AppUserRole> userRolesSet = new HashSet<>();
        Collections.addAll(userRolesSet, userRolesArray);
        Mockito.when(appUserRoleRepository.findAll()).thenReturn(userRolesSet);
    }

    @Test
    void shouldGetAllUserRoles() {
        // when
        Set<AppUserRole> userRoles = appUserRoleService.findAllUserRoles();
        // then
        assertThat(userRoles).isNotEmpty();
        assertThat(userRoles).hasSize(2);
    }
}