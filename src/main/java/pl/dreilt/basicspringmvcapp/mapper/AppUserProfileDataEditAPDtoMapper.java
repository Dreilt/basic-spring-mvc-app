package pl.dreilt.basicspringmvcapp.mapper;

import pl.dreilt.basicspringmvcapp.dto.AppUserProfileDataEditAPDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;

public class AppUserProfileDataEditAPDtoMapper {

    private AppUserProfileDataEditAPDtoMapper() {
    }

    public static AppUserProfileDataEditAPDto mapToAppUserProfileDataEditAPDto(AppUser appUser) {
        return new AppUserProfileDataEditAPDto.AppUserProfileDataEditAPDtoBuilder()
                .withFirstName(appUser.getFirstName())
                .withLastName(appUser.getLastName())
                .withBio(appUser.getBio())
                .withCity(appUser.getCity())
                .build();
    }
}
