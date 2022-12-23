package pl.dreilt.basicspringmvcapp.mapper;

import org.springframework.data.domain.Page;
import pl.dreilt.basicspringmvcapp.dto.AppUserTableAPDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;

public class AppUserTableAPDtoMapper {

    private AppUserTableAPDtoMapper() {
    }

    public static Page<AppUserTableAPDto> mapToAppUserTableAPDtos(Page<AppUser> appUsers) {
        return appUsers.map(appUser -> mapToAppUserTableAPDto(appUser));
    }

    private static AppUserTableAPDto mapToAppUserTableAPDto(AppUser appUser) {
        return new AppUserTableAPDto.AppUserTableAPDtoBuilder()
                .withId(appUser.getId())
                .withFirstName(appUser.getFirstName())
                .withLastName(appUser.getLastName())
                .withEmail(appUser.getEmail())
                .withEnabled(appUser.isEnabled())
                .withAccountNonLocked(appUser.isAccountNonLocked())
                .withRoles(appUser.getRoles())
                .build();

    }
}
