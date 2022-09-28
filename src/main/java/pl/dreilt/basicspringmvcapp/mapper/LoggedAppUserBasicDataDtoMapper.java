package pl.dreilt.basicspringmvcapp.mapper;

import pl.dreilt.basicspringmvcapp.dto.LoggedAppUserBasicDataDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;

import java.util.Base64;

public class LoggedAppUserBasicDataDtoMapper {

    public static LoggedAppUserBasicDataDto mapToLoggedAppUserBasicDataDto(AppUser appUser) {
        LoggedAppUserBasicDataDto loggedAppUserBasicDataDto = new LoggedAppUserBasicDataDto();
        loggedAppUserBasicDataDto.setFirstName(appUser.getFirstName());
        loggedAppUserBasicDataDto.setLastName(appUser.getLastName());
        loggedAppUserBasicDataDto.setAvatarType(appUser.getProfileImage().getFileType());
        loggedAppUserBasicDataDto.setAvatar(Base64.getEncoder().encodeToString(appUser.getProfileImage().getFileData()));
        return loggedAppUserBasicDataDto;
    }
}
