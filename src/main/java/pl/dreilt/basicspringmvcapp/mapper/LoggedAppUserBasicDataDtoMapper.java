package pl.dreilt.basicspringmvcapp.mapper;

import pl.dreilt.basicspringmvcapp.dto.LoggedAppUserBasicDataDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;

public class LoggedAppUserBasicDataDtoMapper {

    public static LoggedAppUserBasicDataDto mapToLoggedAppUserBasicDataDto(AppUser appUser) {
        LoggedAppUserBasicDataDto loggedAppUserBasicDataDto = new LoggedAppUserBasicDataDto();
        loggedAppUserBasicDataDto.setFirstName(appUser.getFirstName());
        loggedAppUserBasicDataDto.setLastName(appUser.getLastName());
        return loggedAppUserBasicDataDto;
    }
}
