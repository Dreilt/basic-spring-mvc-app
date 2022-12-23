package pl.dreilt.basicspringmvcapp.mapper;

import pl.dreilt.basicspringmvcapp.dto.AppUserProfileDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;

import java.util.Base64;

public class AppUserProfileDtoMapper {

    private AppUserProfileDtoMapper() {
    }

    public static AppUserProfileDto mapToAppUserProfileDto(AppUser appUser) {
        return new AppUserProfileDto.AppUserProfileDtoBuilder()
                .withFirstName(appUser.getFirstName())
                .withLastName(appUser.getLastName())
                .withAvatarType(appUser.getProfileImage().getFileType())
                .withAvatarData(Base64.getEncoder().encodeToString(appUser.getProfileImage().getFileData()))
                .withEmail(appUser.getEmail())
                .withBio(appUser.getBio())
                .withCity(appUser.getCity())
                .build();
    }
}
