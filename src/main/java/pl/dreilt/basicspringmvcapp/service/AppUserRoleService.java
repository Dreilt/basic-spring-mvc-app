package pl.dreilt.basicspringmvcapp.service;

import org.springframework.stereotype.Service;
import pl.dreilt.basicspringmvcapp.entity.AppUserRole;
import pl.dreilt.basicspringmvcapp.repository.AppUserRoleRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class AppUserRoleService {

    private final AppUserRoleRepository appUserRoleRepository;

    public AppUserRoleService(AppUserRoleRepository appUserRoleRepository) {
        this.appUserRoleRepository = appUserRoleRepository;
    }

    public Set<AppUserRole> findAllAppUserRole() {
        Iterable<AppUserRole> appUserRoles = appUserRoleRepository.findAll();
        Set<AppUserRole> appUserRolesSet = new HashSet<>();
        for (AppUserRole appUserRole : appUserRoles) {
            appUserRolesSet.add(appUserRole);
        }
        return appUserRolesSet;
    }
}
