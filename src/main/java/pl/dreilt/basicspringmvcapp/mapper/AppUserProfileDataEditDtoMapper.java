package pl.dreilt.basicspringmvcapp.mapper;

import pl.dreilt.basicspringmvcapp.dto.AppUserProfileDataEditDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;

public class AppUserProfileDataEditDtoMapper {

    private AppUserProfileDataEditDtoMapper() {
    }

    public static AppUserProfileDataEditDto mapToAppUserProfileEditDto(AppUser appUser) {
        return new AppUserProfileDataEditDto.AppUserProfileDataEditDtoBuilder()
                .withFirstName(appUser.getFirstName())
                .withLastName(appUser.getLastName())
//                .withProfileImage(appUser.getProfileImage())
                .withBio(appUser.getBio())
                .withCity(appUser.getCity())
                .build();
    }
}
