package pl.dreilt.basicspringmvcapp.mapper;

import pl.dreilt.basicspringmvcapp.dto.AppUserBasicDataAdminPanelDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;

public class AppUserBasicDataAdminPanelDtoMapper {

    private AppUserBasicDataAdminPanelDtoMapper() {
    }

    public static AppUserBasicDataAdminPanelDto mapToAppUserBasicDataAdminPanelDto(AppUser appUser) {
        // tutaj można zastosować wzorzec Builder w przyszłości
        AppUserBasicDataAdminPanelDto appUserBasicDataAdminPanelDto = new AppUserBasicDataAdminPanelDto();
        appUserBasicDataAdminPanelDto.setId(appUser.getId());
        appUserBasicDataAdminPanelDto.setFirstName(appUser.getFirstName());
        appUserBasicDataAdminPanelDto.setLastName(appUser.getLastName());
        appUserBasicDataAdminPanelDto.setBio(appUser.getBio());
        appUserBasicDataAdminPanelDto.setCity(appUser.getCity());
        appUserBasicDataAdminPanelDto.setEnabled(appUser.isEnabled());
        appUserBasicDataAdminPanelDto.setAccountNonLocked(appUser.isAccountNonLocked());
        appUserBasicDataAdminPanelDto.setRoles(appUser.getRoles());
        return appUserBasicDataAdminPanelDto;
    }
}
