package pl.dreilt.basicspringmvcapp.mapper;

import pl.dreilt.basicspringmvcapp.dto.AppUserProfileEditDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;

public class AppUserProfileEditDtoMapper {

    private AppUserProfileEditDtoMapper() {
    }

    public static AppUserProfileEditDto mapToAppUserProfileEditDto(AppUser appUser) {
        // tutaj można zastosować wzorzec Builder w przyszłości
        AppUserProfileEditDto appUserProfileEditDto = new AppUserProfileEditDto();
        appUserProfileEditDto.setFirstName(appUser.getFirstName());
        appUserProfileEditDto.setLastName(appUser.getLastName());
        appUserProfileEditDto.setBio(appUser.getBio());
        appUserProfileEditDto.setCity(appUser.getCity());
        return appUserProfileEditDto;
    }
}
