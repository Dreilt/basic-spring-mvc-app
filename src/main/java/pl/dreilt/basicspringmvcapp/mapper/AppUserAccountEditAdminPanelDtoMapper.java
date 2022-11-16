package pl.dreilt.basicspringmvcapp.mapper;

import pl.dreilt.basicspringmvcapp.dto.AppUserAccountEditAdminPanelDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;

public class AppUserAccountEditAdminPanelDtoMapper {

    private AppUserAccountEditAdminPanelDtoMapper() {
    }

    public static AppUserAccountEditAdminPanelDto mapToAppUserAccountEditAdminPanelDto(AppUser appUser) {
        AppUserAccountEditAdminPanelDto appUserAccountEditAdminPanelDto = new AppUserAccountEditAdminPanelDto();
        appUserAccountEditAdminPanelDto.setEnabled(appUser.isEnabled());
        appUserAccountEditAdminPanelDto.setAccountNonLocked(appUser.isAccountNonLocked());
        appUserAccountEditAdminPanelDto.setRoles(appUser.getRoles());
        return appUserAccountEditAdminPanelDto;
    }
}
