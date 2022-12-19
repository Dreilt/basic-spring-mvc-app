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

    public Set<AppUserRole> findAllUserRoles() {
        Iterable<AppUserRole> userRoles = appUserRoleRepository.findAll();
        Set<AppUserRole> userRolesSet = new HashSet<>();
        for (AppUserRole userRole : userRoles) {
            userRolesSet.add(userRole);
        }
        return userRolesSet;
    }
}
