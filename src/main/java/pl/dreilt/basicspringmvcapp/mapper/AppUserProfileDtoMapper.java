package pl.dreilt.basicspringmvcapp.mapper;

import pl.dreilt.basicspringmvcapp.dto.AppUserProfileDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;

public class AppUserProfileDtoMapper {

    private AppUserProfileDtoMapper() {
    }

    public static AppUserProfileDto mapToAppUserProfileDto(AppUser appUser) {
        // tutaj można zastosować wzorzec Builder w przyszłości
        AppUserProfileDto appUserProfileDto = new AppUserProfileDto();
        appUserProfileDto.setFirstName(appUser.getFirstName());
        appUserProfileDto.setLastName(appUser.getLastName());
        appUserProfileDto.setEmail(appUser.getEmail());
        return appUserProfileDto;
    }
}
