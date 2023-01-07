package pl.dreilt.basicspringmvcapp.mapper;

import pl.dreilt.basicspringmvcapp.dto.AppUserProfileDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;

import java.util.Base64;

public class AppUserProfileDtoMapper {

    private AppUserProfileDtoMapper() {
    }

    public static AppUserProfileDto mapToAppUserProfileDto(AppUser user) {
        return new AppUserProfileDto.AppUserProfileDtoBuilder()
                .withFirstName(user.getFirstName())
                .withLastName(user.getLastName())
                .withAvatarType(user.getProfileImage().getFileType())
                .withAvatarData(Base64.getEncoder().encodeToString(user.getProfileImage().getFileData()))
                .withEmail(user.getEmail())
                .withBio(user.getBio())
                .withCity(user.getCity())
                .build();
    }
}
