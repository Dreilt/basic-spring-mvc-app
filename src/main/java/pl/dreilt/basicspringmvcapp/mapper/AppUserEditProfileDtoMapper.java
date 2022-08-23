package pl.dreilt.basicspringmvcapp.mapper;

import pl.dreilt.basicspringmvcapp.dto.AppUserEditProfileDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;

public class AppUserEditProfileDtoMapper {

    public static AppUserEditProfileDto mapToAppUserProfileDto(AppUser appUser) {
        // tutaj można zastosować wzorzec Builder w przyszłości
        AppUserEditProfileDto appUserEditProfileDto = new AppUserEditProfileDto();
        appUserEditProfileDto.setFirstName(appUser.getFirstName());
        appUserEditProfileDto.setLastName(appUser.getLastName());
        return appUserEditProfileDto;
    }
}
