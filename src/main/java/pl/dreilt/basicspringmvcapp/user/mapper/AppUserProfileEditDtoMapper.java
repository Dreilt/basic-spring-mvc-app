package pl.dreilt.basicspringmvcapp.user.mapper;

import pl.dreilt.basicspringmvcapp.user.AppUser;
import pl.dreilt.basicspringmvcapp.user.dto.AppUserProfileEditDto;

public class AppUserProfileEditDtoMapper {

    private AppUserProfileEditDtoMapper() {
    }

    public static AppUserProfileEditDto mapToAppUserProfileEditDto(AppUser user) {
        return new AppUserProfileEditDto.AppUserProfileEditDtoBuilder()
                .withFirstName(user.getFirstName())
                .withLastName(user.getLastName())
//                .withProfileImage(appUser.getProfileImage())
                .withBio(user.getBio())
                .withCity(user.getCity())
                .build();
    }
}
