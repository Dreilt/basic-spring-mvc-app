package pl.dreilt.basicspringmvcapp.user.mapper;

import pl.dreilt.basicspringmvcapp.user.AppUser;
import pl.dreilt.basicspringmvcapp.user.dto.AppUserAccountEditAPDto;

public class AppUserAccountEditAPDtoMapper {

    private AppUserAccountEditAPDtoMapper() {
    }

    public static AppUserAccountEditAPDto mapToAppUserAccountEditAPDto(AppUser user) {
        return new AppUserAccountEditAPDto.AppUserAccountEditAPDtoBuilder()
                .withEnabled(user.isEnabled())
                .withAccountNonLocked(user.isAccountNonLocked())
                .withRoles(user.getRoles())
                .build();
    }
}
