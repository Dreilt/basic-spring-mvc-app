package pl.dreilt.basicspringmvcapp.mapper;

import pl.dreilt.basicspringmvcapp.dto.AppUserAccountDataEditAdminPanelDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;

public class AppUserAccountDataEditAdminPanelDtoMapper {

    private AppUserAccountDataEditAdminPanelDtoMapper() {
    }

    public static AppUserAccountDataEditAdminPanelDto mapToAppUserAccountDataEditAdminPanelDto(AppUser appUser) {
        AppUserAccountDataEditAdminPanelDto appUserAccountDataEditAdminPanelDto = new AppUserAccountDataEditAdminPanelDto();
        appUserAccountDataEditAdminPanelDto.setEnabled(appUser.isEnabled());
        appUserAccountDataEditAdminPanelDto.setAccountNonLocked(appUser.isAccountNonLocked());
        appUserAccountDataEditAdminPanelDto.setRoles(appUser.getRoles());
        return appUserAccountDataEditAdminPanelDto;
    }
}
