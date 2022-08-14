package pl.dreilt.basicspringmvcapp.mapper;

import pl.dreilt.basicspringmvcapp.dto.AppUserCredentialsDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;
import pl.dreilt.basicspringmvcapp.entity.AppUserRole;

import java.util.Set;
import java.util.stream.Collectors;

public class AppUserCredentialsDtoMapper {

    public static AppUserCredentialsDto mapToAppUserCredentialsDto(AppUser appUser) {
        String email = appUser.getEmail();
        String password = appUser.getPassword();
        boolean enabled = appUser.isEnabled();
        boolean accountNonLocked = appUser.isAccountNonLocked();
        Set<String> roles = appUser.getRoles()
                .stream()
                .map(AppUserRole::getName)
                .collect(Collectors.toSet());
        return new AppUserCredentialsDto(email, password, enabled, accountNonLocked, roles);
    }
}
