package pl.dreilt.basicspringmvcapp.mapper;

import pl.dreilt.basicspringmvcapp.dto.AppUserProfileEditAdminPanelDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;

public class AppUserProfileEditAdminPanelDtoMapper {

    private AppUserProfileEditAdminPanelDtoMapper() {
    }

    public static AppUserProfileEditAdminPanelDto mapToAppUserProfileEditAdminPanelDto(AppUser appUser) {
        // tutaj można zastosować wzorzec Builder w przyszłości
        AppUserProfileEditAdminPanelDto appUserProfileEditAdminPanelDto = new AppUserProfileEditAdminPanelDto();
        appUserProfileEditAdminPanelDto.setFirstName(appUser.getFirstName());
        appUserProfileEditAdminPanelDto.setLastName(appUser.getLastName());
        appUserProfileEditAdminPanelDto.setBio(appUser.getBio());
        appUserProfileEditAdminPanelDto.setCity(appUser.getCity());
        return appUserProfileEditAdminPanelDto;
    }
}
