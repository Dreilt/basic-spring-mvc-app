package pl.dreilt.basicspringmvcapp.mapper;

import pl.dreilt.basicspringmvcapp.dto.AppUserProfileEditAPDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;

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
