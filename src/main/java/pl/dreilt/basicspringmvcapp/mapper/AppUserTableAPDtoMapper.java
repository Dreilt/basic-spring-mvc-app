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
        AppUserTableAPDto appUserTableAPDto = new AppUserTableAPDto();
        appUserTableAPDto.setId(appUser.getId());
        appUserTableAPDto.setFirstName(appUser.getFirstName());
        appUserTableAPDto.setLastName(appUser.getLastName());
        appUserTableAPDto.setEmail(appUser.getEmail());
        appUserTableAPDto.setEnabled(appUser.isEnabled());
        appUserTableAPDto.setAccountNonLocked(appUser.isAccountNonLocked());
        appUserTableAPDto.setRoles(appUser.getRoles());
        return appUserTableAPDto;
    }
}
