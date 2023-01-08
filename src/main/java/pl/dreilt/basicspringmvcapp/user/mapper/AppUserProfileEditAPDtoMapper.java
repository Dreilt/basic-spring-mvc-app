package pl.dreilt.basicspringmvcapp.user.mapper;

import pl.dreilt.basicspringmvcapp.user.AppUser;
import pl.dreilt.basicspringmvcapp.user.dto.AppUserProfileEditAPDto;

public class AppUserProfileEditAPDtoMapper {

    private AppUserProfileEditAPDtoMapper() {
    }

    public static AppUserProfileEditAPDto mapToAppUserProfileEditAPDto(AppUser user) {
        return new AppUserProfileEditAPDto.AppUserProfileEditAPDtoBuilder()
                .withFirstName(user.getFirstName())
                .withLastName(user.getLastName())
                .withBio(user.getBio())
                .withCity(user.getCity())
                .build();
    }
}
