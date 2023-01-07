package pl.dreilt.basicspringmvcapp.mapper;

import pl.dreilt.basicspringmvcapp.dto.AppUserProfileEditDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;

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
