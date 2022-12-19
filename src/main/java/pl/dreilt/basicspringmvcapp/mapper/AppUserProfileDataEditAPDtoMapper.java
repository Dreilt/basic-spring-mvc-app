package pl.dreilt.basicspringmvcapp.mapper;

import pl.dreilt.basicspringmvcapp.dto.AppUserProfileDataEditAPDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;

public class AppUserProfileDataEditAPDtoMapper {

    private AppUserProfileDataEditAPDtoMapper() {
    }

    public static AppUserProfileDataEditAPDto mapToAppUserProfileDataEditAPDto(AppUser appUser) {
        AppUserProfileDataEditAPDto appUserProfileDataEditAPDto = new AppUserProfileDataEditAPDto();
        appUserProfileDataEditAPDto.setFirstName(appUser.getFirstName());
        appUserProfileDataEditAPDto.setLastName(appUser.getLastName());
        appUserProfileDataEditAPDto.setBio(appUser.getBio());
        appUserProfileDataEditAPDto.setCity(appUser.getCity());
        return appUserProfileDataEditAPDto;
    }
}
