package pl.dreilt.basicspringmvcapp.mapper;

import pl.dreilt.basicspringmvcapp.dto.AppUserProfileDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;

import java.util.Base64;

public class AppUserProfileDtoMapper {

    private AppUserProfileDtoMapper() {
    }

    public static AppUserProfileDto mapToAppUserProfileDto(AppUser appUser) {
        // tutaj można zastosować wzorzec Builder w przyszłości
        AppUserProfileDto appUserProfileDto = new AppUserProfileDto();
        appUserProfileDto.setFirstName(appUser.getFirstName());
        appUserProfileDto.setLastName(appUser.getLastName());
        appUserProfileDto.setAvatarType(appUser.getProfileImage().getFileType());
        String avatarData = Base64.getEncoder().encodeToString(appUser.getProfileImage().getFileData());
        appUserProfileDto.setAvatarData(avatarData);
        appUserProfileDto.setEmail(appUser.getEmail());
        appUserProfileDto.setBio(appUser.getBio());
        appUserProfileDto.setCity(appUser.getCity());
        return appUserProfileDto;
    }
}
