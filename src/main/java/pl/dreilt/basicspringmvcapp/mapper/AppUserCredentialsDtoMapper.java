package pl.dreilt.basicspringmvcapp.mapper;

import pl.dreilt.basicspringmvcapp.dto.AppUserCredentialsDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;
import pl.dreilt.basicspringmvcapp.entity.AppUserRole;

import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;

public class AppUserCredentialsDtoMapper {

    private AppUserCredentialsDtoMapper() {
    }

    public static AppUserCredentialsDto mapToAppUserCredentialsDto(AppUser appUser) {
        String firstName = appUser.getFirstName();
        String lastName = appUser.getLastName();
        String email = appUser.getEmail();
        String password = appUser.getPassword();
        String avatarType = appUser.getProfileImage().getFileType();
        String avatar = Base64.getEncoder().encodeToString(appUser.getProfileImage().getFileData());
        boolean enabled = appUser.isEnabled();
        boolean accountNonLocked = appUser.isAccountNonLocked();
        Set<String> roles = appUser.getRoles()
                .stream()
                .map(AppUserRole::getName)
                .collect(Collectors.toSet());
        return new AppUserCredentialsDto(firstName, lastName, email, password, avatarType, avatar, enabled, accountNonLocked, roles);
    }
}
