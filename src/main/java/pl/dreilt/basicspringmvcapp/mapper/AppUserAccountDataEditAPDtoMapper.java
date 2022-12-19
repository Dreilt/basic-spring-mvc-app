package pl.dreilt.basicspringmvcapp.mapper;

import pl.dreilt.basicspringmvcapp.dto.AppUserAccountDataEditAPDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;

public class AppUserAccountDataEditAPDtoMapper {

    private AppUserAccountDataEditAPDtoMapper() {
    }

    public static AppUserAccountDataEditAPDto mapToAppUserAccountDataEditAPDto(AppUser appUser) {
        AppUserAccountDataEditAPDto appUserAccountDataEditAPDto = new AppUserAccountDataEditAPDto();
        appUserAccountDataEditAPDto.setEnabled(appUser.isEnabled());
        appUserAccountDataEditAPDto.setAccountNonLocked(appUser.isAccountNonLocked());
        appUserAccountDataEditAPDto.setRoles(appUser.getRoles());
        return appUserAccountDataEditAPDto;
    }
}
